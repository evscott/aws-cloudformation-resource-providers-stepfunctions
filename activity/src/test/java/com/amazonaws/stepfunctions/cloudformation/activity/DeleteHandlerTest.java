package com.amazonaws.stepfunctions.cloudformation.activity;

import com.amazonaws.services.stepfunctions.model.DeleteActivityRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.cloudformation.proxy.OperationStatus;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class DeleteHandlerTest extends HandlerTestBase {

    private DeleteHandler handler = new DeleteHandler();

    private ResourceHandlerRequest<ResourceModel> request;

    @BeforeEach
    public void setup() {
        request = ResourceHandlerRequest.<ResourceModel>builder()
                .region(REGION)
                .awsAccountId(AWS_ACCOUNT_ID)
                .desiredResourceState(ResourceModel.builder().arn(ACTIVITY_ARN).name(ACTIVITY_NAME).build())
                .build();
    }

    @Test
    public void testSuccess() {
        final ProgressEvent<ResourceModel, CallbackContext> response
            = handler.handleRequest(proxy, request, null, logger);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(OperationStatus.SUCCESS);
        assertThat(response.getCallbackContext()).isNull();
        assertThat(response.getCallbackDelaySeconds()).isEqualTo(0);
        assertThat(response.getResourceModel()).isEqualTo(request.getDesiredResourceState());
        assertThat(response.getResourceModels()).isNull();
        assertThat(response.getMessage()).isNull();
        assertThat(response.getErrorCode()).isNull();
    }

    @Test
    public void test400() {
        Mockito.when(proxy.injectCredentialsAndInvoke(Mockito.any(DeleteActivityRequest.class), Mockito.any(Function.class))).thenThrow(exception400);

        final ProgressEvent<ResourceModel, CallbackContext> response
                = handler.handleRequest(proxy, request, null, logger);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(OperationStatus.FAILED);
    }

    @Test
    public void test500() {
        Mockito.when(proxy.injectCredentialsAndInvoke(Mockito.any(DeleteActivityRequest.class), Mockito.any(Function.class))).thenThrow(exception500);

        final ProgressEvent<ResourceModel, CallbackContext> response
                = handler.handleRequest(proxy, request, null, logger);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(OperationStatus.FAILED);
        assertThat(response.getMessage()).isEqualTo(exception500.getMessage());
    }

    @Test
    public void testUnknownException() {
        Mockito.when(proxy.injectCredentialsAndInvoke(Mockito.any(DeleteActivityRequest.class), Mockito.any(Function.class))).thenThrow(unknownException);

        final ProgressEvent<ResourceModel, CallbackContext> response
                = handler.handleRequest(proxy, request, null, logger);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(OperationStatus.FAILED);
        assertThat(response.getMessage()).isEqualTo(Constants.INTERNAL_FAILURE_MESSAGE);
    }

}