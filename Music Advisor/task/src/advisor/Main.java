package advisor;

import advisor.userInterface.UserInterface;
import org.apache.commons.validator.routines.UrlValidator;

import java.net.MalformedURLException;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        parseAndStart(args);
    }

    private static void parseAndStart(String[] args) throws MalformedURLException {
        if (args.length <= 0) UserInterface.start();
        if (args.length > 6) throw new IllegalArgumentException("Only 3 arguments allowed");
        String paramAccessUrl = null;
        String paramResourceUrl = null;
        int paramPage = 5;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-access")) {
                if (UrlValidator.getInstance().isValid(args[i + 1])) {
                    paramAccessUrl = args[i + 1];
                    i++;
                } else throw new MalformedURLException("Access url is not valid " + args[i + 1]);
            } else if (args[i].equals("-resource")) {
                if (UrlValidator.getInstance().isValid(args[i + 1])) {
                    paramResourceUrl = args[i + 1];
                    i++;
                } else throw new MalformedURLException("Resource url is not valid " + args[i + 1]);
            } else if (args[i].equals("-page")) {
                paramPage = Integer.parseInt(args[i + 1]);
                i++;
            }
            else throw new IllegalArgumentException("Only -access or -resource arguments allowed");
        }

        UserInterface.start(paramAccessUrl, paramResourceUrl, paramPage);
    }

//        if (args[0].equalsIgnoreCase("-access")) UserInterface.start(args[1]);
//        else throw new IllegalArgumentException("Only -access argument allowed");


}