package io.cloud.gcp.compute;
import com.google.cloud.compute.v1.Allowed;
import com.google.cloud.compute.v1.Firewall;
import com.google.cloud.compute.v1.Firewall.Direction;
import com.google.cloud.compute.v1.FirewallsClient;
import com.google.cloud.compute.v1.InsertFirewallRequest;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class CreateFirewallRule {

    public static void main(String[] args)
            throws IOException, ExecutionException, InterruptedException {

    /* project: project ID or project number of the Cloud project you want to use.
       firewallRuleName: name of the rule that is created.
       network: name of the network the rule will be applied to. Available name formats:
        * https://www.googleapis.com/compute/v1/projects/{project_id}/global/networks/{network}
        * projects/{project_id}/global/networks/{network}
        * global/networks/{network} */
        String project = "qea-sandbox";
        String firewallRuleName = "firewall-rule-name-1" + UUID.randomUUID();
        String network = "global/networks/default";

        // The rule will be created with default priority of 1000.
        createFirewall(project, firewallRuleName, network);
    }

    // Creates a simple firewall rule allowing for incoming HTTP and
    // HTTPS access from the entire Internet.
    public static void createFirewall(String project, String firewallRuleName, String network)
            throws IOException, ExecutionException, InterruptedException {
    /* Initialize client that will be used to send requests. This client only needs to be created
       once, and can be reused for multiple requests. After completing all of your requests, call
       the `firewallsClient.close()` method on the client to safely
       clean up any remaining background resources. */
        try (FirewallsClient firewallsClient = FirewallsClient.create()) {

            // The below firewall rule is created in the default network.
            Firewall firewallRule = Firewall.newBuilder()
                    .setName(firewallRuleName)
                    .setDirection(Direction.INGRESS.toString())
                    .addAllowed(
                            Allowed.newBuilder().addPorts("20").addPorts("50-60").setIPProtocol("tcp").build())
                    .addSourceRanges("0.0.0.0/0")
                    .setNetwork(network)
                    .addTargetTags("web")
                    .setDescription("Allowing TCP traffic on port 20 and 50-60 from Internet.")
                    .build();

      /* Note that the default value of priority for the firewall API is 1000.
         If you check the value of `firewallRule.getPriority()` at this point it
         will be equal to 0, however it is not treated as "set" by the library and thus
         the default will be applied to the new rule. If you want to create a rule that
         has priority == 0, you'll need to explicitly set it so: setPriority(0) */

            InsertFirewallRequest insertFirewallRequest = InsertFirewallRequest.newBuilder()
                    .setFirewallResource(firewallRule)
                    .setProject(project).build();

            firewallsClient.insertAsync(insertFirewallRequest).get();

            System.out.println("Firewall rule created successfully -> " + firewallRuleName);
        }
    }
}
