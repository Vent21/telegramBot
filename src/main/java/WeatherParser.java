import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


class WeatherParser {
    private static final String WEATHER_URL =
            "http://api.openweathermap.org/data/2.5/weather?q=Novocheboksarsk,ru" +
                    "&units=metric&appid=be11fcc97dd17d784f017797f5c00703";

    static String parseCurrentWeatherJson() {
        String resultJson = Parser.parseUrl(Parser.createUrl(WEATHER_URL));
        StringBuilder sb = new StringBuilder();
        try {
            JSONObject weatherJsonObject = (JSONObject) JSONValue.parseWithException(resultJson);
            // получаем название города, для которого смотрим погоду
            //sb.append(formatDate(weatherJsonObject.get("dt").toString()) + "\n");
            sb.append("Погода в: " + weatherJsonObject.get("name") + "\n");
            JSONObject temperatureData = (JSONObject) weatherJsonObject.get("main");
            sb.append("Текущая температура воздуха: " + temperatureData.get("temp") + " °C\n");
            sb.append("Атмосферное давление: " + pressureConvert(temperatureData.get("pressure").toString()) + " мм рт.ст.\n");
            sb.append("Влажность: " + temperatureData.get("humidity") + " %" + "\n");
            //sb.append("Минимальная температура воздуха: " + temperatureData.get("temp_min") + " °C" + "\n");
            //sb.append("Максимальная температура воздуха: " + temperatureData.get("temp_max") + " °C" + "\n");
            JSONArray weatherArray = (JSONArray) weatherJsonObject.get("weather");
            JSONObject weatherData = (JSONObject) weatherArray.get(0);
            sb.append("Атмосферные явления: " + weatherId(weatherData.get("id").toString()) + "\n");
            //sb.append("Облачность: " + weatherData.get("main") + "\n");
            sb.append("Дальность видимости: " + weatherJsonObject.get("visibility") + " метров" + "\n");
            JSONObject windData = (JSONObject) weatherJsonObject.get("wind");
            sb.append("Скорость ветра: " + windData.get("speed") + " м/c" + "\n");
            sb.append("Сила ветра: " + windPower(windData.get("speed").toString()) + "\n");
            //sb.append("Направление ветра: " + windData.get("deg") + " градусов");
            JSONObject dateData = (JSONObject) weatherJsonObject.get("sys");
            sb.append("Рассвет: " + formatDate(dateData.get("sunrise").toString()) + "\n");
            sb.append("Закат: " + formatDate(dateData.get("sunset").toString()) + "\n");

        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static String windPower(String string){
        double windSpeed = -1.0;
        try {
            windSpeed = Double.parseDouble(string);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        if(windSpeed >= 0 && windSpeed <= 0.5){
            return "штиль, отсутствие ветра";
        }
        else if(windSpeed >= 0.6 && windSpeed <= 1.7) {
            return "тихий, дым отклоняется от вертикального направления";
        }
        else if(windSpeed >= 1.8 && windSpeed <= 3.3) {
            return "легкий, движение воздуха можно определить лицом, шелестят листья";
        }
        else if(windSpeed >= 3.4 && windSpeed <= 5.2) {
            return "слабый, заметно колебание листьев деревьев, развеваются легкие флаги";
        }
        else if(windSpeed >= 5.3 && windSpeed <= 7.4) {
            return "умеренный, колеблются тонкие ветки, поднимается пыль, клочки бумаги";
        }
        else if(windSpeed >= 7.5 && windSpeed <= 9.8) {
            return "свежий, колеблются большие ветки, на воде поднимаются волны";
        }
        else if(windSpeed >= 9.9 && windSpeed <= 12.4) {
            return "сильный, раскачиваются большие ветки, гудят провода";
        }
        else if(windSpeed >= 12.5 && windSpeed <= 19.2) {
            return "крепкий, качаются стволы небольших деревьев, на водоемах пенятся волны";
        }
        else if(windSpeed >= 19.3 && windSpeed <= 23.2) {
            return "буря, ломаются ветви, движение человека против ветра затруднено";
        }
        else if(windSpeed >= 23.3 && windSpeed <= 26.5) {
            return "сильная буря, срываются домовые трубы и черепица с крыши, повреждаются легкие постройки";
        }
        else if(windSpeed >= 26.6 && windSpeed <= 30.1) {
            return "полная буря, деревья вырываются с корнем, происходят значительные разрушения легких построек";
        }
        else return "";
    }

    private static String pressureConvert(String string){
        double pressureGP = Double.parseDouble(string);
        double pressureMM = pressureGP * 0.750064;
        Integer result = (int) pressureMM;
        return result.toString();
    }

    private static String formatDate(String string){
        long time = 0;
        try {
            time = Long.parseLong(string);
        } catch (NumberFormatException e){
            e.printStackTrace();
        }
        SimpleDateFormat date = new SimpleDateFormat("HH:mm");
        date.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        return date.format(new Date(time * 1000L));
    }

    private static String weatherId(String stringId){
        int id = 0;
        String weather = "null";
        try {
            id = Integer.parseInt(stringId);
        } catch (NumberFormatException e){
            e.printStackTrace();
        }
        switch (id){
            //Виды грозы
            case 200 : weather = "Гроза с небольшим дождем"; break;
            case 201 : weather = "Гроза с дождем"; break;
            case 202 : weather = "Гроза с сильным дождем"; break;
            case 210 : weather = "Легкая гроза"; break;
            case 211 : weather = "Гроза"; break;
            case 212 : weather = "Сильная гроза"; break;
            case 221 : weather = "Гроза с порывистым ветром"; break;
            case 230 : weather = "Гроза с легкой моросью"; break;
            case 231 : weather = "Гроза с моросью"; break;
            case 232 : weather = "Гроза с сильной моросью"; break;
            //Виды мороси
            case 300 : weather = "Легкая морось"; break;
            case 301 : weather = "Морось"; break;
            case 302 : weather = "Сильная морось"; break;
            case 310 : weather = "Легкий моросящий дождь"; break;
            case 311 : weather = "Моросящий дождь"; break;
            case 312 : weather = "Сильный моросящий дождь"; break;
            case 313 : weather = "Дождь с моросью"; break;
            case 314 : weather = "Сильный дождь с моросью"; break;
            case 321 : weather = "Ливень с моросью"; break;
            //Виды дождя
            case 500 : weather = "Легкий дождь"; break;
            case 501 : weather = "Умеренный дождь"; break;
            case 502 : weather = "Интенсивный дождь"; break;
            case 503 : weather = "Сильный дождь"; break;
            case 504 : weather = "Очень сильный дождь"; break;
            case 511 : weather = "Ледяной дождь"; break;
            case 520 : weather = "Легкий ливень"; break;
            case 521 : weather = "Ливень"; break;
            case 522 : weather = "Сильный ливень"; break;
            case 531 : weather = "Рваный ливень"; break;
            //Снег
            case 600 : weather = "Небольшой снег"; break;
            case 601 : weather = "Снег"; break;
            case 602 : weather = "Сильный снег"; break;
            case 611 : weather = "Снег с мелким дождем"; break;
            case 612 : weather = "Мокрый снег"; break;
            case 615 : weather = "Легкий дождь со снегом"; break;
            case 616 : weather = "Снег с дождем"; break;
            case 620 : weather = "Легкий снегопад"; break;
            case 621 : weather = "Снегопад"; break;
            case 622 : weather = "Сильный снегопад"; break;
            //Атмосферные явления
            case 701 : weather = "Низкий туман"; break;
            case 711 : weather = "Дымка"; break;
            case 721 : weather = "Мгла"; break;
            case 731 : weather = "Пылевые завихрения"; break;
            case 741 : weather = "Туман"; break;
            case 751 : weather = "Песок"; break;
            case 761 : weather = "Пыль"; break;
            case 762 : weather = "Вулканический пепел"; break;
            case 781 : weather = "Торнадо"; break;
            //Чистое небо
            case 800 : weather = "Чистое небо"; break;
            //Облачность
            case 801 : weather = "Небольшая облачность"; break;
            case 802 : weather = "Рассеянные облака"; break;
            case 803 : weather = "Разрозненные облака"; break;
            case 804 : weather = "Облачность"; break;
            //Экстримальная погода
        }
        return weather;
    }

}

