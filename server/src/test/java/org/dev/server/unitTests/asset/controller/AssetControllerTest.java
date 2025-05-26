package org.dev.server.unitTests.asset.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.dev.server.config.SecurityDisabledConfig;
import org.dev.server.controller.AssetController;
import org.dev.server.dto.asset.AssetRequestDto;
import org.dev.server.dto.asset.AssetResponseDto;
import org.dev.server.jwtconfig.JWTUtil;
import org.dev.server.model.Asset;
import org.dev.server.service.AssetService;
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

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AssetController.class)
//pentru testare
@Import(SecurityDisabledConfig.class)
@ActiveProfiles("test")
public class AssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AssetService assetService;


    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JWTUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private AssetRequestDto request;
    private AssetResponseDto response;
    private Asset asset;

    @BeforeEach
    public void initTestData() {
        Faker faker = new Faker();

        String rawSymbol =  faker.cryptoCoin().coin(); // ETH, BTC
        String symbol = rawSymbol.substring(0, Math.min(10, rawSymbol.length()));

        String rawName = faker.company().name();
        String name = rawName.substring(0, Math.min(50, rawName.length()));

        Double price = faker.number().randomDouble(2, 0, 10000);

        request = new AssetRequestDto(symbol, name, price);
        asset = new Asset(1L, request.symbol(), request.name(), request.price());
        response = new AssetResponseDto(1L, request.symbol(), request.name(), request.price());
    }

    @Test
    void createAsset_shouldReturnCreated() throws Exception {
        when(assetService.createAsset(any())).thenReturn(response);

        mockMvc.perform(post("/asset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.assetId").value(response.assetId()))
                .andExpect(jsonPath("$.symbol").value(response.symbol()))
                .andExpect(jsonPath("$.name").value(response.name()))
                .andExpect(jsonPath("$.price").value(response.price()));
    }

    @Test
    void getAllAssets_shouldReturnList() throws Exception {
        when(assetService.getAllAssets()).thenReturn(List.of(response));

        mockMvc.perform(get("/asset"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].assetId").value(response.assetId()))
                .andExpect(jsonPath("$[0].symbol").value(response.symbol()));
    }

    @Test
    void getAsset_shouldReturnAssetById() throws Exception {
        when(assetService.getAsset(1L)).thenReturn(response);

        mockMvc.perform(get("/asset/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assetId").value(response.assetId()))
                .andExpect(jsonPath("$.name").value(response.name()));
    }

    @Test
    void updateAsset_shouldReturnUpdatedAsset() throws Exception {
        when(assetService.updateAsset(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/asset/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assetId").value(response.assetId()))
                .andExpect(jsonPath("$.symbol").value(response.symbol()));
    }

    @Test
    void deleteAsset_shouldReturnNoContent() throws Exception {
        doNothing().when(assetService).deleteAsset(1L);

        mockMvc.perform(delete("/asset/1"))
                .andExpect(status().isNoContent());

        verify(assetService).deleteAsset(1L);
    }

}
