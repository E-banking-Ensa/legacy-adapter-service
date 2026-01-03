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

    public record TransactionDTO(int accountId, double amount, int destId, String motif, String type) {}

    // DTO pour la recharge
    public record RechargeRequestDTO(int accountId, double amount, String phoneNumber) {}

    @PostMapping("/depot")
    public Map<String, Object> depot(@RequestBody TransactionDTO dto) {
        me.polytech.ebanking_soap.gen.DepotResponse res = soapClient.depot(dto.accountId(), dto.amount());
        return Map.of("status", res.getStatus(), "solde", res.getMontant());
    }

    @PostMapping("/retrait")
    public Map<String, Object> retrait(@RequestBody TransactionDTO dto) {
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

    // Endpoint pour la recharge téléphonique
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
}