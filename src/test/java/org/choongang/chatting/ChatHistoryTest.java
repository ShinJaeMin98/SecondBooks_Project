package org.choongang.chatting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.choongang.chatting.controllers.ChatHistorySearch;
import org.choongang.chatting.controllers.RequestChatHistory;
import org.choongang.chatting.controllers.RequestChatRoom;
import org.choongang.chatting.entities.ChatHistory;
import org.choongang.chatting.entities.ChatRoom;
import org.choongang.chatting.service.ChatHistoryInfoService;
import org.choongang.chatting.service.ChatRoomSaveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(properties = "spring.profiles.active=test")
public class ChatHistoryTest {

    @Autowired
    private ChatRoomSaveService chatRoomSaveService;

    @Autowired
    private ChatHistoryInfoService chatHistoryInfoService;

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper om;

    private RequestChatRoom chatRoomForm;

    @BeforeEach
    void init(){
        om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());

        chatRoomForm = new RequestChatRoom();
        chatRoomForm.setRoomId("room502");
        chatRoomForm.setRoomNm("502호");
        chatRoomSaveService.save(chatRoomForm);
    }

    @Test
    @DisplayName("채팅 기록 저장 테스트")
    void chatHistorySave () throws Exception {

        RequestChatHistory form = new RequestChatHistory();
        form.setRoomId(chatRoomForm.getRoomId());
        form.setMessage("테스트 메시지");
        form.setNickName("응가누");

        String params = om.writeValueAsString(form);

        mockMvc.perform(post("/api/chat")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(params))
                .andExpect(status().isCreated());
        ChatHistorySearch search = new ChatHistorySearch();
        List<ChatHistory> items = chatHistoryInfoService.getList(form.getRoomId(), search);

        assertEquals(1, items.size());
        items.forEach(System.out::println);
    }
}
