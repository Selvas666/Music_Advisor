package advisor.userInterface;

import advisor.external.facade.*;
import advisor.external.model.Authorization;
import advisor.external.model.Pagarium;
import advisor.userInterface.model.Command;
import advisor.userInterface.model.CommandWord;
import io.vavr.collection.List;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;
import se.michaelthelin.spotify.model_objects.specification.Paging;

import java.net.URI;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

public class UserInterface {

    private static Authorization authorization = Authorization.builder()
            .result(false)
            .build();
    private static String accessUrl;
    private static URI resourceUrl;
    private static SpotifyApi spotifyApi;
    private static int page = 5;
    private static Pagarium currentList;
    private static FeaturedFacade featuredFacade;
    private static NewFacade newFacade;
    private static CategoriesFacade categoriesFacade;
    private static PlaylistsFacade playlistsFacade;
//    private static ApiEnum apiEnum;
//    private static int currentPagesNo;


    public static void start() {
        featuredFacade = new FeaturedFacade();
        newFacade = new NewFacade();
        categoriesFacade = new CategoriesFacade();
        playlistsFacade = new PlaylistsFacade();
        if (Objects.isNull(accessUrl)) accessUrl = "https://accounts.spotify.com";
        if (Objects.isNull(resourceUrl)) resourceUrl = URI.create("https://api.spotify.com");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.hasNext()) {
                try {
                    Command currentCommand = Command.parseCommand(scanner.nextLine()).orElseThrow();
                    if (currentCommand.getCommandWord().equals(CommandWord.EXIT)) {
                        System.out.println("---GOODBYE!---");
//                        break;
                    }
                    execute(currentCommand);
                } catch (NoSuchElementException ex) {
                    System.out.println("No such command found!");
                }

            }
        }
    }

    public static void start (String paramAccessUrl, String paramResourceUrl) {
        System.out.println(paramAccessUrl);
        accessUrl = paramAccessUrl;
        System.out.println(paramResourceUrl);
        resourceUrl = URI.create(paramResourceUrl);
        start();
    }

    public static void start (String paramAccessUrl, String paramResourceUrl, int paramPage) {
        System.out.println("Page is " + paramPage);
        page = paramPage;
        start (paramAccessUrl, paramResourceUrl);
    }


    public static void execute(Command command) {
        if (command.getCommandWord().equals(CommandWord.AUTH)){
            authorization = AuthorizationController.authorize(accessUrl);
            spotifyApi = new SpotifyApi.Builder()
                    .setAccessToken(authorization.getToken())
                    .setHost(resourceUrl.getHost())
                    .setPort(resourceUrl.getPort())
                    .setScheme(resourceUrl.getScheme())
                    .build();
            System.out.println(authorization.getCommunicate());
        }
        if (!authorization.isResult()){
            System.out.println("Please, provide access for application.");
        }
        switch (command.getCommandWord()) {
            case NEW:
//                apiEnum = ApiEnum.NEW;
                currentList = new Pagarium (newFacade.showNew(spotifyApi, page), page);
                currentList.printNext();
                break;
            case FEATURED:
                currentList = new Pagarium (featuredFacade.showFeatured(spotifyApi, page), page);
                currentList.printNext();
                break;
            case PLAYLISTS:
                String categoriesParam = command.getParameter();
                currentList = new Pagarium(playlistsFacade.showPlaylists(categoriesParam, spotifyApi, page), page);
                currentList.printNext();
                break;
            case CATEGORIES:
                currentList = new Pagarium(categoriesFacade.showCategories(spotifyApi, page),page);
                currentList.printNext();
                break;
            case NEXT:
                currentList.printNext();
                break;
            case PREV:
                currentList.printPrev();
                break;
        }
    }
}
