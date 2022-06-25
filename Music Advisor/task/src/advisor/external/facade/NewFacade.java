package advisor.external.facade;

import io.vavr.collection.List;
import org.apache.hc.core5.http.ParseException;
import org.apache.http.entity.ContentType;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.requests.data.browse.GetListOfNewReleasesRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class NewFacade {

    public List<String> showNew(SpotifyApi spotifyApi, int page) {

        final GetListOfNewReleasesRequest getListOfNewReleasesRequest = spotifyApi.getListOfNewReleases()
//          .country(CountryCode.SE)
          .limit(page)
//          .offset(0)
                .build();

        try {
            final Paging<AlbumSimplified> albumSimplifiedPaging = getListOfNewReleasesRequest.execute();

            return printAlbums(albumSimplifiedPaging);

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("FAILED TO GET NEW RELEASES!");

        return null;
    }

//    public Paging<AlbumSimplified> getAnother(String url, SpotifyApi spotifyApi) {
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
//            final Paging<AlbumSimplified> albumSimplifiedPaging = new AlbumSimplified.JsonUtil().createModelObjectPaging(response.body(), "albums");
//            return printAlbums(albumSimplifiedPaging);
//
//        } catch (InterruptedException | IOException ex) {
//            System.out.println("Failed to get another page of Playlists.");
//            return null;
//        }
//
//    }

    private List <String> printAlbums (Paging<AlbumSimplified> albumSimplifiedPaging) {
        return List.of (albumSimplifiedPaging.getItems())
                .map(item -> {
                    String itemName = item.getName() + "\n";
                    String url = item.getExternalUrls().getExternalUrls().get("spotify");
                    List <String> artists = List.of(item.getArtists())
                            .map(ArtistSimplified::getName)
                            .collect(List.collector());
                    String artistString = "[";
                    for (int i = 0; i < artists.size(); i++) {
                        artistString = artistString + artists.get(i);
                        if (i+1 < artists.size()) artistString = artistString + ", ";
                    }
                    artistString = artistString + "]";
                    return itemName + artistString + "\n" + url;
                })
                .collect(List.collector());
    }

}
