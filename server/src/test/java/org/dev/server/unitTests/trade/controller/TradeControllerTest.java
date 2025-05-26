package org.dev.server.unitTests.trade.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.dev.server.config.SecurityDisabledConfig;
import org.dev.server.controller.TradeController;
import org.dev.server.dto.trade.TradeRequestDto;
import org.dev.server.dto.trade.TradeResponseDto;
import org.dev.server.jwtconfig.JWTUtil;
import org.dev.server.service.TradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.trilead.ssh2.auth.AuthenticationManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TradeController.class)
@Import(SecurityDisabledConfig.class)
@ActiveProfiles("test")
public class TradeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TradeService service;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JWTUtil jwtUtil;

    @MockitoBean
    private AuthenticationManager authenticationManager;
    @Autowired
    private ObjectMapper mapper;

    private TradeRequestDto req;
    private TradeResponseDto resp;
    private UUID buyerId, sellerId, tid;
    private Long buyOid, sellOid;
    private final String symbol = "BTC";
    private BigDecimal price, quantity;
    private LocalDateTime executedAt;

    @BeforeEach
    void init() {
        Faker faker = new Faker();
        buyerId  = UUID.randomUUID();
        sellerId = UUID.randomUUID();
        buyOid   = 1L;
        sellOid  = 2L;
        tid      = UUID.randomUUID();
        price    = BigDecimal.valueOf(faker.number().randomDouble(2, 1, 50000));
        quantity = BigDecimal.valueOf(faker.number().randomDouble(2, 1, 10));
        executedAt = LocalDateTime.now();

        req = new TradeRequestDto(
                buyerId, sellerId, buyOid, sellOid, 1L,
                price, quantity, executedAt
        );

        resp = new TradeResponseDto(
                tid, buyerId, sellerId, buyOid, sellOid,
                symbol, price, quantity, executedAt
        );
    }

    @Test
    void createTrade_shouldReturnCreated() throws Exception {
        when(service.createTrade(any(TradeRequestDto.class))).thenReturn(resp);

        mockMvc.perform(post("/api/trades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tradeId").value(tid.toString()))
                .andExpect(jsonPath("$.buyerId").value(buyerId.toString()));

        verify(service).createTrade(any(TradeRequestDto.class));
    }

    @Test
    void byAsset_shouldReturnList() throws Exception {
        when(service.findByAssetSymbol(symbol)).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/trades/asset/{sym}", symbol))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].assetSymbol").value(symbol));

        verify(service).findByAssetSymbol(symbol);
    }

    @Test
    void byBuyer_shouldReturnList() throws Exception {
        when(service.findByBuyer_Id(buyerId)).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/trades/buyer/{id}", buyerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].buyerId").value(buyerId.toString()));

        verify(service).findByBuyer_Id(buyerId);
    }

    @Test
    void bySeller_shouldReturnList() throws Exception {
        when(service.findBySeller_Id(sellerId)).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/trades/seller/{id}", sellerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].sellerId").value(sellerId.toString()));

        verify(service).findBySeller_Id(sellerId);
    }

    @Test
    void byExecutedBetween_shouldReturnList() throws Exception {
        LocalDateTime from = executedAt.minusDays(1);
        LocalDateTime to = executedAt.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        when(service.findByExecutedAtBetween(from, to)).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/trades/executed")
                        .param("from", from.format(formatter))
                        .param("to", to.format(formatter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tradeId").value(tid.toString()));

        verify(service).findByExecutedAtBetween(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void byPriceOver_shouldReturnList() throws Exception {
        BigDecimal minPrice = BigDecimal.valueOf(1000);

        when(service.findByPriceGreaterThan(minPrice)).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/trades/price-over/{price}", minPrice))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].price").value(price.doubleValue()));

        verify(service).findByPriceGreaterThan(minPrice);
    }
}