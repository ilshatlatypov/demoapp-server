package ru.jvdev.demoapp.server;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ru.jvdev.demoapp.server.entity.Role;
import ru.jvdev.demoapp.server.entity.Task;
import ru.jvdev.demoapp.server.entity.User;
import ru.jvdev.demoapp.server.repository.TaskRepository;
import ru.jvdev.demoapp.server.repository.UserRepository;

/**
 * Created by ilshat on 22.09.16.
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskRepository taskRepository;

    @Value("${spring.profiles.active}")
    String springActiveProfile;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    String jpaDdlAuto;

    @Override
    public void run(String... args) throws Exception {
        if (springActiveProfile.equals("dev") && jpaDdlAuto.startsWith("create")) {
            reloadData();
        }
    }

    // CHECKSTYLE:OFF MultipleStringLiteralsCheck
    // CHECKSTYLE:OFF MagicNumberCheck
    private void reloadData() {
        Set<User> users = getUsers();
        userRepository.deleteAllInBatch();
        userRepository.save(users);

        Set<Task> tasks = getTasks();
        taskRepository.deleteAllInBatch();
        List<Task> savedTasks = taskRepository.save(tasks);

        User user = userRepository.findByUsername("emp");
        Random r = new Random();
        List<Integer> randomTaskIds = savedTasks.stream()
            .filter(t -> r.nextInt(10) % 10 == 0)
            .map(Task::getId)
            .collect(Collectors.toList());
        taskRepository.setUserIdForTasks(user.getId(), randomTaskIds);
    }
    // CHECKSTYLE:ON MagicNumberCheck

    private Set<User> getUsers() {
        // names generated by http://freegenerator.ru/fio, transliterated by http://translit-online.ru/yandex.html
        Set<User> users = new HashSet<>();
        users.add(new User("Менеджер", "Менеджеров", "man", "man", Role.MANAGER));
        users.add(new User("Сотрудник", "Сотрудников", "emp", "emp", Role.EMPLOYEE));
        users.add(new User("Поликарп", "Котов", "pkotov", "pkotov", Role.EMPLOYEE));
        users.add(new User("Любовь", "Яковлева", "lyakovleva", "lyakovleva", Role.EMPLOYEE));
        users.add(new User("Лиана", "Дубровина", "ldubrovina", "ldubrovina", Role.MANAGER));
        users.add(new User("Агафон", "Безбородов", "abezborodov", "abezborodov", Role.EMPLOYEE));
        users.add(new User("Ян", "Ясеневский", "yayasenevskij", "yayasenevskij", Role.EMPLOYEE));
        users.add(new User("Бронислав", "Агафонов", "bagafonov", "bagafonov", Role.EMPLOYEE));
        users.add(new User("Агафон", "Яромеев", "ayaromeev", "ayaromeev", Role.EMPLOYEE));
        users.add(new User("Егор", "Капустов", "ekapustov", "ekapustov", Role.EMPLOYEE));
        users.add(new User("Доминика", "Балтабева", "dbaltabeva", "dbaltabeva", Role.EMPLOYEE));
        users.add(new User("Софья", "Морозова", "smorozova", "smorozova", Role.MANAGER));
        users.add(new User("Зоя", "Тамаркина", "ztamarkina", "ztamarkina", Role.EMPLOYEE));
        users.add(new User("Оксана", "Набиуллина", "onabiullina", "onabiullina", Role.EMPLOYEE));
        users.add(new User("Лавр", "Мандрыкин", "lmandrykin", "lmandrykin", Role.EMPLOYEE));
        users.add(new User("Дементий", "Шлыков", "dshlykov", "dshlykov", Role.EMPLOYEE));
        users.add(new User("Егор", "Другаков", "edrugakov", "edrugakov", Role.EMPLOYEE));
        users.add(new User("Мирослав", "Маланов", "mmalanov", "mmalanov", Role.EMPLOYEE));
        users.add(new User("Валерьян", "Щерба", "vshcherba", "vshcherba", Role.MANAGER));
        users.add(new User("Иннокентий", "Григорьев", "igrigorev", "igrigorev", Role.EMPLOYEE));
        users.add(new User("Кристина", "Путина", "kputina", "kputina", Role.EMPLOYEE));
        users.add(new User("Валерьян", "Эшман", "vehshman", "vehshman", Role.EMPLOYEE));
        users.add(new User("Виктория", "Невшупа", "vnevshupa", "vnevshupa", Role.EMPLOYEE));
        users.add(new User("Чеслав", "Помельников", "chpomelnikov", "chpomelnikov", Role.EMPLOYEE));
        users.add(new User("Андрон", "Вишняков", "avishnyakov", "avishnyakov", Role.EMPLOYEE));
        users.add(new User("Аким", "Эсце", "aehsce", "aehsce", Role.MANAGER));
        users.add(new User("Ульяна", "Ануфриева", "uanufrieva", "uanufrieva", Role.EMPLOYEE));
        users.add(new User("Кристина", "Злобина", "kzlobina", "kzlobina", Role.EMPLOYEE));
        users.add(new User("Нина", "Кортнева", "nkortneva", "nkortneva", Role.EMPLOYEE));
        users.add(new User("Агафья", "Подшивалова", "apodshivalova", "apodshivalova", Role.EMPLOYEE));
        users.add(new User("Виталий", "Степанишин", "vstepanishin", "vstepanishin", Role.EMPLOYEE));
        users.add(new User("Сократ", "Соколов", "ssokolov", "ssokolov", Role.MANAGER));
        return users;
    }

    private Set<Task> getTasks() {
        LocalDate today = LocalDate.now();
        final int[] daysFromToday = {0};
        final Random r = new Random();
        final int bound = 5;

        Supplier<LocalDate> dateSupplier =
            () -> today.plusDays(r.nextInt(bound) == 0 ? ++daysFromToday[0] : daysFromToday[0]);

        Set<Task> tasks = new HashSet<>();
        tasks.add(new Task("Заснуть под звездами", dateSupplier.get()));
        tasks.add(new Task("Выключить свой мобильник на неделю", dateSupplier.get()));
        tasks.add(new Task("Поплавать с дельфинами", dateSupplier.get()));
        tasks.add(new Task("Нырнуть с аквалангом", dateSupplier.get()));
        tasks.add(new Task("Попробовать текилу в Мексике", dateSupplier.get()));
        tasks.add(new Task("Выучить английский", dateSupplier.get()));
        tasks.add(new Task("Полазить по деревьям", dateSupplier.get()));
        tasks.add(new Task("Пускать мыльные пузыри", dateSupplier.get()));
        tasks.add(new Task("Написать историю своей жизни", dateSupplier.get()));
        tasks.add(new Task("Написать письмо и отправить его в бутылке в море", dateSupplier.get()));
        tasks.add(new Task("Посадить дерево", dateSupplier.get()));
        tasks.add(new Task("Увидеть пингвинов", dateSupplier.get()));
        tasks.add(new Task("Научиться танцевать сальсу", dateSupplier.get()));
        tasks.add(new Task("Создать свой бизнес", dateSupplier.get()));
        tasks.add(new Task("Влюбиться без памяти", dateSupplier.get()));
        tasks.add(new Task("Побыть членом жюри", dateSupplier.get()));
        tasks.add(new Task("Протанцевать всю ночь", dateSupplier.get()));
        tasks.add(new Task("Постоять под водопадом", dateSupplier.get()));
        tasks.add(new Task("Встретить Хеллоуин в Америке", dateSupplier.get()));
        tasks.add(new Task("Полежать на берегу океана и послушать шум волн", dateSupplier.get()));
        tasks.add(new Task("Научиться кататься на коньках", dateSupplier.get()));
        tasks.add(new Task("Поучаствовать в карнавале в Венеции", dateSupplier.get()));
        tasks.add(new Task("Написать свой план на год и следовать ему", dateSupplier.get()));
        tasks.add(new Task("Наблюдать лунное затмение", dateSupplier.get()));
        tasks.add(new Task("Встретить Новый год в экзотическом месте", dateSupplier.get()));
        tasks.add(new Task("Прыгнуть с парашютом", dateSupplier.get()));
        tasks.add(new Task("Полюбить себя", dateSupplier.get()));
        tasks.add(new Task("Пригласить незнакомца на свидание", dateSupplier.get()));
        tasks.add(new Task("Купить очень дорогую одежду", dateSupplier.get()));
        tasks.add(new Task("Научиться водить машину", dateSupplier.get()));
        tasks.add(new Task("Побывать в Париже", dateSupplier.get()));
        tasks.add(new Task("Провести целый день с книгой", dateSupplier.get()));
        tasks.add(new Task("Встретить настоящих друзей", dateSupplier.get()));
        tasks.add(new Task("Увидеть закат на Бали", dateSupplier.get()));
        tasks.add(new Task("Увидеть вживую 7 чудес света", dateSupplier.get()));
        tasks.add(new Task("Прыгнуть в море со скалы", dateSupplier.get()));
        tasks.add(new Task("Преодолеть страх неудачи", dateSupplier.get()));
        tasks.add(new Task("Поиграть с коалами", dateSupplier.get()));
        tasks.add(new Task("Отправиться в морское путешествие", dateSupplier.get()));
        tasks.add(new Task("Разыграть по-крупному друзей", dateSupplier.get()));
        tasks.add(new Task("Забраться на Эйфелеву башню", dateSupplier.get()));
        tasks.add(new Task("Сделать пожертвование анонимно", dateSupplier.get()));
        tasks.add(new Task("Сделать сюрприз любимому человеку", dateSupplier.get()));
        tasks.add(new Task("Покурить кальян", dateSupplier.get()));
        tasks.add(new Task("Создать свой сайт или блог", dateSupplier.get()));
        tasks.add(new Task("Потусить на Ибице", dateSupplier.get()));
        tasks.add(new Task("Сделать свое генеалогическое дерево", dateSupplier.get()));
        tasks.add(new Task("Провести выходные в спа-центре", dateSupplier.get()));
        tasks.add(new Task("Пробежать марафон", dateSupplier.get()));
        tasks.add(new Task("Покататься на сноуборде", dateSupplier.get()));
        tasks.add(new Task("Медитировать 3 часа", dateSupplier.get()));
        tasks.add(new Task("Подержать в ладонях живую бабочку", dateSupplier.get()));
        tasks.add(new Task("Проехаться верхом на лошади по побережью", dateSupplier.get()));
        tasks.add(new Task("Познакомиться с кем-нибудь на улице", dateSupplier.get()));
        tasks.add(new Task("Отдать бездомному свой завтрак", dateSupplier.get()));
        return tasks;
    }
    // CHECKSTYLE:ON MultipleStringLiteralsCheck
}