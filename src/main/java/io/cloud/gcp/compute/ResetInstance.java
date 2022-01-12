package io.cloud.gcp.compute;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.compute.v1.InstancesClient;
import com.google.cloud.compute.v1.Operation;
import com.google.cloud.compute.v1.Operation.Status;
import com.google.cloud.compute.v1.ResetInstanceRequest;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
public class ResetInstance {

    public static void main(String[] args)
            throws IOException, ExecutionException, InterruptedException {

    /* project: project ID or project number of the Cloud project your instance belongs to.
       zone: name of the zone your instance belongs to.
       instanceName: name of the instance your want to reset.
     */
        String project = "qea-sandbox";
        String zone = "us-central1-c";
        String instanceName = "instance-name3";

        resetInstance(project, zone, instanceName);
    }

    // Resets a stopped Google Compute Engine instance (with unencrypted disks).
    public static void resetInstance(String project, String zone, String instanceName)
            throws IOException, ExecutionException, InterruptedException {
    /* Initialize client that will be used to send requests. This client only needs to be created
       once, and can be reused for multiple requests. After completing all of your requests, call
       the `instancesClient.close()` method on the client to safely
       clean up any remaining background resources. */
        try (InstancesClient instancesClient = InstancesClient.create()) {

            ResetInstanceRequest resetInstanceRequest = ResetInstanceRequest.newBuilder()
                    .setProject(project)
                    .setZone(zone)
                    .setInstance(instanceName)
                    .build();

            OperationFuture<Operation, Operation> operation = instancesClient.resetAsync(
                    resetInstanceRequest);
            Operation response = operation.get();

            if (response.getStatus() == Status.DONE) {
                System.out.println("Instance reset successfully ! ");
            }
        }
    }
}
