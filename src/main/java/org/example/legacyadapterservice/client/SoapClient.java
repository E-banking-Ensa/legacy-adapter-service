package org.example.legacyadapterservice.client;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import java.math.BigInteger;

public class SoapClient extends WebServiceGatewaySupport {

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

    public me.polytech.ebanking_soap.gen.VirementResponse virement(int source, int dest, double montant) {
        me.polytech.ebanking_soap.gen.VirementRequest request = new me.polytech.ebanking_soap.gen.VirementRequest();
        // Conversion int -> BigInteger car le XSD utilise xs:integer
        request.setSource(BigInteger.valueOf(source));
        request.setDest(BigInteger.valueOf(dest));
        request.setMontant(montant);

        return (me.polytech.ebanking_soap.gen.VirementResponse) getWebServiceTemplate().marshalSendAndReceive(
                request,
                new SoapActionCallback("http://www.polytech.me/ebanking-soap/VirementRequest")
        );
    }
}