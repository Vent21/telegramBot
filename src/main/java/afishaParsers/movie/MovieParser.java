package afishaParsers.movie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieParser {
    private static String h1Element = "";
    private static final String AFISHA_CHEB = "https://afisha.cheb.ru/kino/";
    private static Map<String, List<Movie>> cinema = new HashMap<>(250);

    public static String parseMovieShow(String cinemaKey){
        try {
            Document document = Jsoup.connect(AFISHA_CHEB).get();
            Elements h1 = document.getElementsByTag("h1");
            h1Element = h1.get(0).ownText();

            Elements cinemaAndMovies = document.select("table");

            for (Element cinemaAndMovie : cinemaAndMovies) {
                String cinemaName = cinemaAndMovie.select("b").get(0).ownText();
                /*
                 Кинотеатр "Синема 5"
                 Кинотеатр "Три пингвина"
                 Кинотеатр "Волжский"
                 Кинотеатр "Мир Луксор"
                 Кинотеатр "Киногалактика"
                 Кинозал "Сеспель"
                 Кинотеатр "Тетерин Фильм"
                 Кинозал в ДК "Химик"
                 */
                cinema.put(cinemaName, getMoviesInElements(cinemaAndMovie));
            }

        } catch (IOException e) {
            return "Невозможно отобразить киноафишу: \n" + e.toString();
        }
        return movieListToString(cinemaKey, h1Element);
    }

    private static List<Movie> getMoviesInElements(Element element){
        List<Movie> movies = new ArrayList<>();
        Elements trTagElements = element.getElementsByTag("tr");

        for(int i = 1; i < trTagElements.size(); i++){
            Element trTagElement = trTagElements.get(i);

            String time = trTagElement.select("td").first().ownText();
            String movieName = "❌ Error unknown movie! ❌";

            try {
                movieName = trTagElement.getElementsByTag("a").first().ownText();
            }catch (NullPointerException ignored){
                //если вебмастер не выставит тег ссылку на фильм
            }

            String description = "";
            String duration = "";

            Elements spanElements = trTagElement.select("span");
            if(spanElements.size() == 3){
                    description = spanElements.get(0).ownText();
                    //spanElements.get(1).ownText(); возрастной ценз
                    duration = spanElements.get(2).ownText();
            }

            Elements nobr = trTagElement.select("nobr");
            String cost = "";
            String room = "";
            if(nobr.size() == 2){
                cost = nobr.first().ownText();
                room = nobr.get(1).ownText();
            }
            movies.add(new Movie(time, movieName, description, duration, cost, room));
        }
        return movies;
    }

    private static String movieListToString(String key, String h1){
        StringBuilder sb = new StringBuilder(h1 + ", в " + key + ":\n");
        sb.append(" \n");
        List<Movie> list = cinema.get(key);
        list.forEach(value -> sb.append(value).append("\n"));
        return sb.toString();
    }
}


