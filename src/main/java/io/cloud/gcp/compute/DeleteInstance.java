package io.cloud.gcp.compute;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.compute.v1.DeleteInstanceRequest;
import com.google.cloud.compute.v1.InstancesClient;
import com.google.cloud.compute.v1.Operation;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class DeleteInstance {

    public static void main(String[] args)
            throws IOException, InterruptedException, ExecutionException {

        String project = "qea-sandbox";
        String zone = "us-central1-a";
        String instanceName = "instance-name3";
        deleteInstance(project, zone, instanceName);
    }

    // Delete the instance specified by `instanceName`
    // if it's present in the given project and zone.
    public static void deleteInstance(String project, String zone, String instanceName)
            throws IOException, InterruptedException, ExecutionException {
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the `instancesClient.close()` method on the client to safely
        // clean up any remaining background resources.
        try (InstancesClient instancesClient = InstancesClient.create()) {

            System.out.printf("Deleting instance: %s ", instanceName);

            // Describe which instance is to be deleted.
            DeleteInstanceRequest deleteInstanceRequest = DeleteInstanceRequest.newBuilder()
                    .setProject(project)
                    .setZone(zone)
                    .setInstance(instanceName).build();

            OperationFuture<Operation, Operation> operation = instancesClient.deleteAsync(
                    deleteInstanceRequest);
            // Wait for the operation to complete.
            Operation response = operation.get();

            if (response.hasError()) {
                System.out.println("Instance deletion failed ! ! " + response);
                return;
            }
            System.out.println("Operation Status: " + response.getStatus());
        }
    }
}
