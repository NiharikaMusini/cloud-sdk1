package io.cloud.gcp.compute;

import com.google.cloud.compute.v1.Image;
import com.google.cloud.compute.v1.ImagesClient;
import com.google.cloud.compute.v1.ImagesClient.ListPage;
import com.google.cloud.compute.v1.ListImagesRequest;
import java.io.IOException;
public class ListImages {

    public static void main(String[] args) throws IOException {

        // project: project ID or project number of the Cloud project you want to list images from.
        String project = "qea-sandbox";
        listImages(project);

        // page_size: size of the pages you want the API to return on each call.
        int pageSize = 100;
        listImagesByPage(project, pageSize);
    }

    // [START compute_images_list]
    // Prints a list of all non-deprecated image names available in given project.
    public static void listImages(String project) throws IOException {
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the `instancesClient.close()` method on the client to
        // safely clean up any remaining background resources.
        try (ImagesClient imagesClient = ImagesClient.create()) {

            // Listing only non-deprecated images to reduce the size of the reply.
            ListImagesRequest imagesRequest = ListImagesRequest.newBuilder()
                    .setProject(project)
                    .setMaxResults(100)
                    .setFilter("deprecated.state != DEPRECATED")
                    .build();

            // Although the `setMaxResults` parameter is specified in the request, the iterable returned
            // by the `list()` method hides the pagination mechanic. The library makes multiple
            // requests to the API for you, so you can simply iterate over all the images.
            int imageCount = 0;
            for (Image image : imagesClient.list(imagesRequest).iterateAll()) {
                imageCount++;
                System.out.println(image.getName());
            }
            System.out.printf("Image count in %s is: %s", project, imageCount);
        }
    }
    // [END compute_images_list]

    // [START compute_images_list_page]
    //  Prints a list of all non-deprecated image names available in a given project,
    //  divided into pages as returned by the Compute Engine API.
    public static void listImagesByPage(String project, int pageSize) throws IOException {
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the `instancesClient.close()` method on the client to
        // safely clean up any remaining background resources.
        try (ImagesClient imagesClient = ImagesClient.create()) {

            // Listing only non-deprecated images to reduce the size of the reply.
            ListImagesRequest imagesRequest = ListImagesRequest.newBuilder()
                    .setProject(project)
                    .setMaxResults(pageSize)
                    .setFilter("deprecated.state != DEPRECATED")
                    .build();

            // Use the `iteratePages` attribute of returned iterable to have more granular control of
            // iteration over paginated results from the API. Each time you want to access the
            // next page, the library retrieves that page from the API.
            int pageNumber = 1;
            for (ListPage page : imagesClient.list(imagesRequest).iteratePages()) {
                System.out.println("Page Number: " + pageNumber++);
                for (Image image : page.getValues()) {
                    System.out.println(image.getName());
                }
            }
        }
    }
}
