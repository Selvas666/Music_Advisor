package advisor.external.facade;

import advisor.external.exceptions.UnknownCategoryException;
import io.vavr.collection.List;
import org.apache.hc.core5.http.ParseException;
import org.apache.http.entity.ContentType;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Category;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.requests.data.browse.GetCategorysPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.browse.GetListOfCategoriesRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;

public class PlaylistsFacade {
    public List<String> showPlaylists(String category, SpotifyApi spotifyApi, int page) {

        final GetListOfCategoriesRequest getListOfCategoriesRequest = spotifyApi.getListOfCategories()
                .limit(page)
//          .country(CountryCode.SE)
//          .limit(10)
//          .offset(0)
//          .locale("sv_SE")
                .build();

        String categoryId = "";

        try {
            final Paging<Category> categoryPaging = getListOfCategoriesRequest.execute();

            System.out.println(category);
            System.out.println(Arrays.toString(categoryPaging.getItems()));

            categoryId = List.of(categoryPaging.getItems())
                    .find(n -> n.getName().equalsIgnoreCase(category))
                    .getOrElseThrow(UnknownCategoryException::new)
                    .getId();

//            String url = spotifyApi.getScheme() + "://" + spotifyApi.getHost() + "/v1/browse/categories/" + categoryId + "/playlists";
//
//            HttpRequest request = HttpRequest.newBuilder()
//                    .header("Content-Type", String.valueOf(ContentType.APPLICATION_JSON))
//                    .header("Authorization", spotifyApi.getAccessToken())
//                    .uri(URI.create(url))
//                    .GET()
//                    .timeout(Duration.ofMillis(10000))
//                    .build();
//
//            HttpClient client = HttpClient.newBuilder().build();
//
//            HttpResponse<String> response;
//            response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
//            return List.of(jo.getAsString());


            final GetCategorysPlaylistsRequest getCategoryRequest = spotifyApi.getCategorysPlaylists(categoryId)
//          .country(CountryCode.SE)
//          .limit(10)
//          .offset(0)
                    .build();
            final Paging<PlaylistSimplified> playlistSimplifiedPaging = getCategoryRequest.execute();

            return List.of(playlistSimplifiedPaging.getItems())
                    .map(n -> n.getName() + "\n" + n.getExternalUrls().getExternalUrls().get("spotify"))
                    .collect(List.collector());

//            return playlistSimplifiedPaging;

        } catch (IOException | SpotifyWebApiException | ParseException e) {

            System.out.println("Error: " + e.getMessage());
        } catch (UnknownCategoryException e) {
            System.out.println("Unknown category name.");
            return null;
        } catch (NullPointerException e) {
            System.out.println("Test unpredictable error message");
            return null;
        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println("FAILED TO GET CATEGORY PLAYLISTS!");
        return null;
    }

//    public Paging<PlaylistSimplified> getAnother(String url, SpotifyApi spotifyApi) {
//
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
//            final Paging<PlaylistSimplified> playlistSimplifiedPaging = new PlaylistSimplified.JsonUtil().createModelObjectPaging(response.body(), "playlists");
//            List.of(playlistSimplifiedPaging.getItems())
//                    .map(n -> n.getName() + "\n" + n.getExternalUrls().getExternalUrls().get("spotify"))
//                    .collect(List.collector()).forEach(System.out::println);
//
//            return playlistSimplifiedPaging;
//        } catch (InterruptedException | IOException ex) {
//            System.out.println("Failed to get another page of Playlists.");
//            return null;
//        }
//
//    }


}
