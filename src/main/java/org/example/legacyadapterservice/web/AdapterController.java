package org.example.legacyadapterservice.web;

import org.example.legacyadapterservice.client.SoapClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/legacy")
public class AdapterController {

    private final SoapClient soapClient;

    public AdapterController(SoapClient soapClient) {
        this.soapClient = soapClient;
    }

    // --- DTOs ---
    public record TransactionDTO(int accountId, double amount, int destId, String motif, String type) {}
    public record DepotRetraitDTO(int accountId, double amount) {}
    public record RechargeRequestDTO(int accountId, double amount, String phoneNumber) {}


    // --- ENDPOINTS BANCAIRES ---

    @PostMapping("/depot")
    public Map<String, Object> depot(@RequestBody DepotRetraitDTO dto) {
        me.polytech.ebanking_soap.gen.DepotResponse res = soapClient.depot(dto.accountId(), dto.amount());
        return Map.of("status", res.getStatus(), "solde", res.getMontant());
    }

    @PostMapping("/retrait")
    public Map<String, Object> retrait(@RequestBody DepotRetraitDTO dto) {
        me.polytech.ebanking_soap.gen.RetraitResponse res = soapClient.retrait(dto.accountId(), dto.amount());
        return Map.of("status", res.getStatus(), "solde", res.getMontant());
    }

    @PostMapping("/virement")
    public Map<String, String> virement(@RequestBody TransactionDTO dto) {
        me.polytech.ebanking_soap.gen.VirementResponse res = soapClient.virement(
                dto.accountId(),
                dto.destId(),
                dto.motif(),
                dto.amount(),
                dto.type()
        );
        return Map.of("status", res.getStatus());
    }

    @PostMapping("/recharge")
    public Map<String, Object> recharge(@RequestBody RechargeRequestDTO dto) {
        me.polytech.ebanking_soap.gen.RechargeResponse res = soapClient.recharge(
                dto.accountId(),
                dto.amount(),
                dto.phoneNumber()
        );
        return Map.of(
                "status", res.getStatus(),
                "transactionId", res.getTransactionId()
        );
    }

    // Dans AdapterController.java

    // Mise à jour des DTOs pour inclure "exchangeRate"
    public record CryptoBuyRequestDTO(int accountId, long walletId, double amount, String currency, double exchangeRate) {}
    public record CryptoSellRequestDTO(int accountId, long walletId, double cryptoAmount, double exchangeRate) {}

    @PostMapping("/crypto/buy")
    public Map<String, Object> buyCrypto(@RequestBody CryptoBuyRequestDTO dto) {
        me.polytech.ebanking_soap.gen.BuyCryptoResponse res = soapClient.buyCrypto(
                dto.accountId(),
                dto.walletId(),
                dto.amount(),
                dto.currency(),
                dto.exchangeRate() // <-- On prend le taux du JSON
        );
        // ... retour inchangé
        return Map.of("status", res.getStatus(), "cryptoReceived", res.getCryptoAmountReceived());
    }

    @PostMapping("/crypto/sell")
    public Map<String, Object> sellCrypto(@RequestBody CryptoSellRequestDTO dto) {
        me.polytech.ebanking_soap.gen.SellCryptoResponse res = soapClient.sellCrypto(
                dto.accountId(),
                dto.walletId(),
                dto.cryptoAmount(),
                dto.exchangeRate() // <-- On prend le taux du JSON
        );
        // ... retour inchangé
        return Map.of("status", res.getStatus(), "fiatReceived", res.getFiatAmountReceived());
    }
}