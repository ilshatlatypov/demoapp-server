package ru.jvdev.demoapp.server.repository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;

import ru.jvdev.demoapp.server.entity.Task;
import ru.jvdev.demoapp.server.utils.Paths;
import ru.jvdev.demoapp.server.utils.PropertyNames;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 09.09.2016
 */
public class TaskDateTest extends AbstractSpringTest {

    private static final String TITLE = "Any title";

    @Autowired
    private TaskRepository taskRepository;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private LocalDate today = LocalDate.now();

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        //noinspection OptionalGetWithoutIsPresent
        mappingJackson2HttpMessageConverter = Arrays.stream(converters)
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny().get();
        assertNotNull("the JSON message converter must not be null", mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        initMockMvc();
    }

    @Test
    public void testTodayIsOkOnCreate() throws Exception {
        String taskJson = json(new Task(TITLE, today));
        mockMvc.perform(post(Paths.TASKS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(taskJson))
            .andExpect(status().isCreated());
    }

    @Test
    public void testFutureDateIsOkOnCreate() throws Exception {
        LocalDate tomorrow = today.plusDays(1);
        String taskJson = json(new Task(TITLE, tomorrow));
        mockMvc.perform(post(Paths.TASKS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(taskJson))
            .andExpect(status().isCreated());
    }

    @Test
    public void testPastDateIsNotOkOnCreate() throws Exception {
        LocalDate yesterday = today.minusDays(1);
        String taskJson = json(new Task(TITLE, yesterday));
        mockMvc.perform(post(Paths.TASKS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(taskJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors", hasSize(1)))
            .andExpect(jsonPath("$.errors[0].property", is(PropertyNames.DATE)))
            .andExpect(jsonPath("$.errors[0].message", is("must be today or future date")));
    }

    @Test
    public void testTodayIsOkOnUpdate() throws Exception {
        LocalDate anyDate = LocalDate.now();
        Task overdueTask = new Task(TITLE, anyDate);
        overdueTask = taskRepository.save(overdueTask);

        overdueTask.setDate(today);

        String taskJson = json(overdueTask);
        mockMvc.perform(put("/tasks/" + overdueTask.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(taskJson))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testFutureDateIsOkOnUpdate() throws Exception {
        LocalDate anyDate = LocalDate.now();
        Task overdueTask = new Task(TITLE, anyDate);
        overdueTask = taskRepository.save(overdueTask);

        LocalDate tomorrow = today.plusDays(1);
        overdueTask.setDate(tomorrow);

        String taskJson = json(overdueTask);
        mockMvc.perform(put("/tasks/" + overdueTask.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(taskJson))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testPastDateIsOkIfNotChangedOnUpdate() throws Exception {
        LocalDate yesterday = today.minusDays(1);
        Task overdueTask = new Task(TITLE, yesterday);
        overdueTask = taskRepository.save(overdueTask);

        // update title but not date
        overdueTask.setTitle("new title");

        String taskJson = json(overdueTask);
        mockMvc.perform(put("/tasks/" + overdueTask.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(taskJson))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testPastDateIsNotOkIfChangedOnUpdate() throws Exception {
        LocalDate yesterday = today.minusDays(1);
        Task overdueTask = new Task(TITLE, yesterday);
        int taskId = taskRepository.save(overdueTask).getId();

        LocalDate dayBeforeYesterday = yesterday.minusDays(1);
        Task updatedOverdueTask = new Task(TITLE, dayBeforeYesterday);

        String taskJson = json(updatedOverdueTask);
        mockMvc.perform(put("/tasks/" + taskId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(taskJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors", hasSize(1)))
            .andExpect(jsonPath("$.errors[0].property", is(PropertyNames.DATE)))
            .andExpect(jsonPath("$.errors[0].message", is("must be today or future date")));
    }

    @Test
    public void testNullDateIsNotOkOnCreate() throws Exception {
        String taskJson = json(new Task(TITLE, null));
        mockMvc.perform(post(Paths.TASKS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(taskJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors", hasSize(1)))
            .andExpect(jsonPath("$.errors[0].property", is(PropertyNames.DATE)))
            .andExpect(jsonPath("$.errors[0].message", is("may not be null")));
    }

    @Test
    public void testNullDateIsNotOkOnUpdate() throws Exception {
        LocalDate anyDate = LocalDate.now();
        Task task = taskRepository.save(new Task(TITLE, anyDate));

        task.setDate(null);

        String taskJson = json(task);
        mockMvc.perform(put("/tasks/" + task.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(taskJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors", hasSize(1)))
            .andExpect(jsonPath("$.errors[0].property", is(PropertyNames.DATE)))
            .andExpect(jsonPath("$.errors[0].message", is("may not be null")));
    }

    @SuppressWarnings("unchecked")
    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
