package backend.server.DTO.myPage;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyPageMemberDTO {

    private String name;

    private String department;

    private String password;

    private int totalTime;

    private String profilePicture;
}
