package com.example.momogum.service.appointmentService;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
class AppointmentServiceTest {

//    @Autowired
//    private PostService postService;
//    @Autowired
//    private PostRepository postRepository;
//    @Autowired
//    private UserRepository userRepository;
//
//    @BeforeEach
//    void clean() {
//        postRepository.deleteAll();
//        userRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("글 작성")
//    void test1() {
//        //given
//        var user = User.builder()
//                .name("TravelLog")
//                .email("jmmmm@naver.com")
//                .password("1234")
//                .build();
//        userRepository.save(user);
//
//        PostCreate postCreate = PostCreate.builder()
//                .title("제목입니다.")
//                .content("내용입니다.")
//                .build();
//
//        //when
//        postService.write(user.getId(), postCreate);
//
//        //then
//        assertEquals(1L, postRepository.count());
//
//        Post post = postRepository.findAll().get(0);
//        assertEquals("제목입니다.", post.getTitle());
//        assertEquals("내용입니다.", post.getContent());
//
//    }

}