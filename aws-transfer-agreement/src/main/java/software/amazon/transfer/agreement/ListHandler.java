package software.amazon.transfer.agreement;

import software.amazon.awssdk.services.transfer.TransferClient;
import software.amazon.awssdk.services.transfer.model.InternalServiceErrorException;
import software.amazon.awssdk.services.transfer.model.InvalidRequestException;
import software.amazon.awssdk.services.transfer.model.ListAgreementsRequest;
import software.amazon.awssdk.services.transfer.model.ListAgreementsResponse;
import software.amazon.awssdk.services.transfer.model.TransferException;
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException;
import software.amazon.cloudformation.exceptions.CfnInvalidRequestException;
import software.amazon.cloudformation.exceptions.CfnServiceInternalErrorException;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.OperationStatus;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

import java.util.ArrayList;
import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ListHandler extends BaseHandler<CallbackContext> {
    private TransferClient client;

    public ListHandler(TransferClient client) {
        this.client = client;
    }

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
        final AmazonWebServicesClientProxy proxy,
        final ResourceHandlerRequest<ResourceModel> request,
        final CallbackContext callbackContext,
        final Logger logger) {

        if (this.client == null) {
            this.client = ClientBuilder.getClient();
        }

        final ResourceModel topModel = request.getDesiredResourceState();
        final List<ResourceModel> models = new ArrayList<>();

        ListAgreementsRequest listAgreementsRequest = ListAgreementsRequest.builder()
                .serverId(topModel.getServerId())
                .maxResults(10)
                .nextToken(request.getNextToken())
                .build();

        try {
            ListAgreementsResponse response =
                    proxy.injectCredentialsAndInvokeV2(listAgreementsRequest, client::listAgreements);

            response.agreements().forEach(listedAgreement -> {
                ResourceModel model = ResourceModel.builder()
                        .arn(listedAgreement.arn())
                        .agreementId(listedAgreement.agreementId())
                        .description(listedAgreement.description())
                        .status(listedAgreement.status().name())
                        .serverId(listedAgreement.serverId())
                        .localProfileId(listedAgreement.localProfileId())
                        .partnerProfileId(listedAgreement.partnerProfileId())
                        .build();
                models.add(model);
            });

            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .resourceModels(models)
                    .nextToken(response.nextToken())
                    .status(OperationStatus.SUCCESS)
                    .build();
        } catch (InvalidRequestException e) {
            throw new CfnInvalidRequestException(listAgreementsRequest.toString(), e);
        } catch (InternalServiceErrorException e) {
            throw new CfnServiceInternalErrorException("listAgreement", e);
        } catch (TransferException e) {
            throw new CfnGeneralServiceException(e.getMessage(), e);
        }
    }
}
