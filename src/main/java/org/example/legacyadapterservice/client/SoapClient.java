package org.example.legacyadapterservice.client;

import me.polytech.ebanking_soap.gen.VirementResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import java.math.BigInteger;

public class SoapClient extends WebServiceGatewaySupport {

    // --- OPÉRATIONS BANCAIRES CLASSIQUES ---

    public me.polytech.ebanking_soap.gen.DepotResponse depot(int compte, double montant) {
        me.polytech.ebanking_soap.gen.DepotRequest request = new me.polytech.ebanking_soap.gen.DepotRequest();
        request.setAccount(compte);
        request.setMontant(montant);

        return (me.polytech.ebanking_soap.gen.DepotResponse) getWebServiceTemplate().marshalSendAndReceive(
                request,
                new SoapActionCallback("http://www.polytech.me/ebanking-soap/depotRequest")
        );
    }

    public me.polytech.ebanking_soap.gen.RetraitResponse retrait(int compte, double montant) {
        me.polytech.ebanking_soap.gen.RetraitRequest request = new me.polytech.ebanking_soap.gen.RetraitRequest();
        request.setAccount(compte);
        request.setMontant(montant);

        return (me.polytech.ebanking_soap.gen.RetraitResponse) getWebServiceTemplate().marshalSendAndReceive(
                request,
                new SoapActionCallback("http://www.polytech.me/ebanking-soap/RetraitRequest")
        );
    }

    public me.polytech.ebanking_soap.gen.VirementResponse virement(int source, int dest, String motif, double montant, String type) {
        me.polytech.ebanking_soap.gen.VirementRequest request = new me.polytech.ebanking_soap.gen.VirementRequest();
        // Conversion int -> BigInteger car le XSD utilise xs:integer
        request.setSource(BigInteger.valueOf(source));
        request.setMotif(motif);
        request.setDest(BigInteger.valueOf(dest));
        request.setType(type);
        request.setMontant(montant);

        return (VirementResponse) getWebServiceTemplate()
                .marshalSendAndReceive("http://localhost:8090/ws", request);
    }

    public me.polytech.ebanking_soap.gen.RechargeResponse recharge(int accountId, double amount, String phoneNumber) {
        me.polytech.ebanking_soap.gen.RechargeRequest request = new me.polytech.ebanking_soap.gen.RechargeRequest();
        request.setAccount(accountId);
        request.setMontant(amount);
        request.setPhoneNumber(phoneNumber);

        return (me.polytech.ebanking_soap.gen.RechargeResponse) getWebServiceTemplate()
                .marshalSendAndReceive("http://localhost:8090/ws", request);
    }

    // Dans SoapClient.java

    public me.polytech.ebanking_soap.gen.BuyCryptoResponse buyCrypto(int accountId, long walletId, double fiatAmount, String currency, double rate) {
        me.polytech.ebanking_soap.gen.BuyCryptoRequest request = new me.polytech.ebanking_soap.gen.BuyCryptoRequest();
        request.setAccountId(accountId);
        request.setCryptoWalletId(walletId);
        request.setFiatAmount(fiatAmount);
        request.setCryptoCurrency(currency);
        request.setExchangeRate(rate); // <-- Envoi du taux

        return (me.polytech.ebanking_soap.gen.BuyCryptoResponse) getWebServiceTemplate()
                .marshalSendAndReceive("http://localhost:8090/ws", request);
    }

    public me.polytech.ebanking_soap.gen.SellCryptoResponse sellCrypto(int accountId, long walletId, double cryptoAmount, double rate) {
        me.polytech.ebanking_soap.gen.SellCryptoRequest request = new me.polytech.ebanking_soap.gen.SellCryptoRequest();
        request.setAccountId(accountId);
        request.setCryptoWalletId(walletId);
        request.setCryptoAmount(cryptoAmount);
        request.setExchangeRate(rate); // <-- Envoi du taux

        return (me.polytech.ebanking_soap.gen.SellCryptoResponse) getWebServiceTemplate()
                .marshalSendAndReceive("http://localhost:8090/ws", request);
    }
}