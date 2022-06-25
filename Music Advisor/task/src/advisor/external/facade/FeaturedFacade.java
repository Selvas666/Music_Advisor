package advisor.external.facade;

import io.vavr.collection.List;
import org.apache.hc.core5.http.ParseException;
import org.apache.http.entity.ContentType;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.special.FeaturedPlaylists;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.requests.data.browse.GetListOfFeaturedPlaylistsRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class FeaturedFacade {

    public List<String> showFeatured(SpotifyApi spotifyApi, int page) {

//        HttpClient client = HttpClient.newBuilder().build();
//        URI url = URI.create(resourceUrl + "/v1/browse/featured-playlists");
//        String authorizationBearer = "Bearer " + authorization.getToken();
//
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .header("Content-Type", "application/json")
//                .header("Authorization", authorizationBearer)
//                .uri(url)
//                .GET()
//                .timeout(Duration.ofMillis(10000))
//                .build();
//
//        HttpResponse<String> response;
//
//        try {
//            response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
//
//        }
//        catch (IOException | InterruptedException | JsonSyntaxException ex) {
//            ex.printStackTrace();
//            return List.of("Request failed");
//        }

        final GetListOfFeaturedPlaylistsRequest getListOfFeaturedPlaylistsRequest = spotifyApi
                .getListOfFeaturedPlaylists()

//          .country(CountryCode.SE)
          .limit(page)
//          .offset(0)
//          .timestamp(new Date(1414054800000L))
                .build();
        try {
            final FeaturedPlaylists featuredPlaylists = getListOfFeaturedPlaylistsRequest.execute();
            return List.of(featuredPlaylists.getPlaylists()
                            .getItems())
                    .map(item -> item.getName() + "\n" + item.getExternalUrls().getExternalUrls().get("spotify"))
                    .collect(List.collector());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("FAILURE!");

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
//            final FeaturedPlaylists featuredPlaylists = new FeaturedPlaylists.JsonUtil().createModelObject(response.body());
//
//            final Paging<PlaylistSimplified> playlistSimplifiedPaging = featuredPlaylists.getPlaylists();
//            List.of(playlistSimplifiedPaging.getItems())
//                    .map(n -> n.getName() + "\n" + n.getExternalUrls().getExternalUrls().get("spotify"))
//                    .collect(List.collector()).forEach(System.out::println);
//
//            return playlistSimplifiedPaging;
//        } catch (InterruptedException | IOException ex) {
//            System.out.println("Failed to get another page of featured playlists.");
//            return null;
//        }
//
//    }

}
