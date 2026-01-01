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

    // DTO simple pour recevoir les données JSON
    public record TransactionDTO(int accountId, double amount, int destId,String motif,String type) {}

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
                dto.accountId(), // source
                dto.destId(),    // destination
                dto.motif(),
                dto.amount(),
                dto.type()
        );
        return Map.of("status", res.getStatus());
    }
}