package com.wemakeplay.wemakeplay.domain.team.entity;

import com.wemakeplay.wemakeplay.domain.attendteam.AttendTeam;
import com.wemakeplay.wemakeplay.domain.team.dto.TeamRequestDto;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teams")
@NoArgsConstructor
@Getter
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String teamName;
    @Column(nullable = false, unique = true)
    private String teamIntro;
    @Column
    private int teamPersonnel;

    // 생성자
    @ManyToOne
    private User teamOwner;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<AttendTeam> attendTeams = new ArrayList<>();

    public Team(TeamRequestDto teamRequestDto, User user){
        this.teamName = teamRequestDto.getTeamName();
        this.teamIntro = teamRequestDto.getTeamIntro();
        this.teamPersonnel = teamRequestDto.getTeamPersonnel();
        this.teamOwner = user;
    }

    public void updateTeam(TeamRequestDto teamRequestDto){
        this.teamName = teamRequestDto.getTeamName();
        this.teamIntro = teamRequestDto.getTeamIntro();
        this.teamPersonnel = teamRequestDto.getTeamPersonnel();
    }

    //특정 팀 ID에 해당하는 참가 팀을 조회
//    public void inviteUser(User user){
//        AttendTeam attendTeam = new AttendTeam(this, user, Participation.wait);
//        this.attendTeams.add(attendTeam);
//    }
//}


    // 특정 참가상태 팀 조회
//    private AttendTeam findAttendTeamByUser(User user) {
//        return this.attendTeams.stream()
//            .filter(attendTeam -> attendTeam.getUser().equals(user))
//            .findFirst()
//            .orElse(null);
//    }


//    @Transactional
//    public void withdraw(User user){
//        Optional<AttendTeam> optionalAttendTeam = findAttendTeamByUser(user);
//        if (optionalAttendTeam.isPresent()) {
//            // 참여를 찾았을 경우 삭제합니다.
//            AttendTeam attendTeam = optionalAttendTeam.get();
//            this.attendTeams.remove(attendTeam);
//            attendTeam.setTeam(null); // AttendTeam과의 양방향 연관 관계 해제
//        } else {
//            throw new ServiceException(ErrorCode.NOT_TEAM_MEMBER);
//        }
//    }
//
//    private Optional<AttendTeam> findAttendTeamByUser(User user) {
//        // 특정 사용자의 참여를 찾아 Optional로 반환합니다.
//        return this.attendTeams.stream()
//            .filter(attendTeam -> attendTeam.getUser().equals(user))
//            .findFirst();
//    }
}
