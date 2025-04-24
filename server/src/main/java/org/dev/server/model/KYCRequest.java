package org.dev.server.model;

@Entity
@Table(name = "kyc_request")
public class KYCRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kycRequestId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String document;
    private String selfie;

    @Enumerated(EnumType.STRING)
    private KycStatus status;

    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;

}
