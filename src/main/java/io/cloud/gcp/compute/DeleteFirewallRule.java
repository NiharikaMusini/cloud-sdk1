package io.cloud.gcp.compute;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.compute.v1.FirewallsClient;
import com.google.cloud.compute.v1.Operation;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
public class DeleteFirewallRule {
    public static void main(String[] args)
            throws IOException, ExecutionException, InterruptedException {

        // project: project ID or project number of the Cloud project you want to use.
        // firewallRuleName: name of the firewall rule you want to delete.
        String project = "qea-sandbox";
        String firewallRuleName = "firewall-rule-name-1" + UUID.randomUUID();
        deleteFirewallRule(project, firewallRuleName);
    }


    // Deletes a firewall rule from the project.
    public static void deleteFirewallRule(String project, String firewallRuleName)
            throws IOException, ExecutionException, InterruptedException {
    /* Initialize client that will be used to send requests. This client only needs to be created
       once, and can be reused for multiple requests. After completing all of your requests, call
       the `firewallsClient.close()` method on the client to safely
       clean up any remaining background resources. */
        try (FirewallsClient firewallsClient = FirewallsClient.create()) {

            OperationFuture<Operation, Operation> operation = firewallsClient.deleteAsync(project,
                    firewallRuleName);
            operation.get();

            System.out.println("Deleted firewall rule -> " + firewallRuleName);
        }
    }
}
