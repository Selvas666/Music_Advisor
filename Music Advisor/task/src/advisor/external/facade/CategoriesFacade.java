package advisor.external.facade;

import io.vavr.collection.List;
import org.apache.hc.core5.http.ParseException;
import org.apache.http.entity.ContentType;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Category;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.browse.GetListOfCategoriesRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class CategoriesFacade {

    public List <String> showCategories(SpotifyApi spotifyApi, int page) {

        final GetListOfCategoriesRequest getListOfCategoriesRequest = spotifyApi.getListOfCategories()
//          .country(CountryCode.SE)
          .limit(page)
//          .offset(0)
//          .locale("sv_SE")
                .build();

        try {
            final Paging<Category> categoryPaging = getListOfCategoriesRequest.execute();

//            System.out.println(categoryPaging.toString());
            return List.of(categoryPaging.getItems())
                    .map(Category::getName)
                    .collect(List.collector());

//            return  categoryPaging;


        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;
    }

//    public Paging<Category> getAnother(String url, SpotifyApi spotifyApi) {
//        HttpRequest request = HttpRequest.newBuilder()
//                .header("Content-Type", String.valueOf(ContentType.APPLICATION_JSON))
//                .header("Authorization", spotifyApi.getAccessToken())
//                .uri(URI.create(url))
//                .GET()
//                .timeout(Duration.ofMillis(10000))
//                .build();
//
//        HttpClient client = HttpClient.newBuilder().build();
//
//        HttpResponse<String> response;
//        try {
//            response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            final Paging<Category> categoryPaging = new Category.JsonUtil().createModelObjectPaging(response.body(), "categories");
//            List.of(categoryPaging.getItems())
//                    .map(Category::getName)
//                    .collect(List.collector()).forEach(System.out::println);
//
//            return categoryPaging;
//        } catch (InterruptedException | IOException ex) {
//            System.out.println("Failed to get another page of Categories.");
//            return null;
//        }
//    }
}
