package birthdayReminder;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class BirthdayReminder {
    private static HashMap<String, LocalDate> birthdays = new HashMap<>();

    static {
        birthdays.put("Тест1", LocalDate.of(0, 12, 5));
    }

    public static String getBirthday() {
        LocalDate today = LocalDate.now();
        LocalDate birthday;
        String name;
        String result = "";
        long minDays = Integer.MAX_VALUE;

        for(Map.Entry<String, LocalDate> entry : birthdays.entrySet()) {
            name = entry.getKey();
            birthday = entry.getValue();
            LocalDate nextBDay = birthday.withYear(today.getYear());
            // Если в этом году День Рождения уже прошёл, то добавляем один год.
            // || nextBDay.isEqual(today)
            if (nextBDay.isBefore(today) ) {
                nextBDay = nextBDay.plusYears(1);
            }

            Period p = Period.between(today, nextBDay);
            long days = ChronoUnit.DAYS.between(today, nextBDay);
            if(days < minDays) {
                minDays = days;
                if(days == 0){
                    result = congratulationText(name);
                }else {
                    result = "До день рождения " + name + " " + p.getMonths() + " месяцев, и " +
                            p.getDays() + " дней. (" +
                            days + " дней всего)";
                }
            }
        }
        return result;
    }

    private static String congratulationText(String name){
        String[] textArr = new String[5];
        String openMessage = String.format("Сегодня день рождения %s:\n" , name);

        textArr[0] = String.format("%s\n" +
                "От души хочу поздравить тебя с этим замечательным днем!\n" +
                "Искренне желаю, чтобы тебе всегда сопутствовала удача, чтобы все твои начинания и дела были обречены на успех.\n" +
                "Желаю, чтобы от друзей ты видел искреннюю поддержку, от семьи — любовь, а в карьере пусть одно достижение следует за другим.\n" +
                "С днем рождения тебя!", openMessage);
        textArr[1] = String.format("Поздравляю с днем рождения! %s\n" +
                "Желаю безмерного счастья, крепкого здоровья, удачи, достатка, исполнения желаний!\n" +
                "Пусть жизнь будет наполнена положительными эмоциями, верными друзьями, радостными днями. Ярких, светлых, счастливых тебе событий!", name);
        textArr[2] = String.format("Поздравляю с днем рождения %s!\n" +
                        "Кроме стандартных пожеланий — счастья, здоровья и всего наилучшего — я желаю, " +
                        "чтобы сбывались все мечты, чтобы каждый день приносил много приятных неожиданностей, " +
                        "чтобы тебя окружали только добрые и нужные люди. А еще удчи.\n" +
                        "Чтобы каждое начатое дело заканчивалось успешно!", name);
        textArr[3] = String.format("С днем рождения %s! " +
                "Поздравлять хороших людей всегда приятно, а такого замечательного человека, как ты, — приятно вдвойне.\n" +
                "Я искренне желаю, чтобы у тебя всегда было достаточно: улыбок, солнечных дней, успеха, поздравлений, поддержки, верных друзей, " +
                "гениальных идей, интересных затей, только важных людей, денег, чувств и эмоций.\n" +
                "Но, главное, чтобы в нужную минуту у тебя всегда было бы то, что тебе нужно.", name);
        textArr[4] = String.format("Поздравляю с днем рождения %s!\n" +
                "Удачи тебе во всех начинаниях, исполнения желаний, всегда светлого и хорошего настроения, веры в себя и продвижения к своим целям.\n" +
                "А еще прекрасного здоровья, больше улыбок и в праздники, и в будни, и пусть каждый день будет наполнен счастьем и теплом.", name);

        int index = (int) (Math.random() * 5);

        return textArr[index];
    }
}
