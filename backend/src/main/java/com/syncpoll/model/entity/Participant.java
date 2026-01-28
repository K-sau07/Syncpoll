package com.syncpoll.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "participants")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    // Nullable for OPEN join mode sessions
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    // Nullable for OPEN join mode
    private String email;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL)
    private List<Answer> answers = new ArrayList<>();

    @OneToOne(mappedBy = "participant", cascade = CascadeType.ALL)
    private Attendance attendance;

    public Participant() {}

    // For open join mode - just a name
    public Participant(Session session, String displayName) {
        this.session = session;
        this.displayName = displayName;
    }

    // For verified join mode - linked to a user account
    public Participant(Session session, User user) {
        this.session = session;
        this.user = user;
        this.displayName = user.getName();
        this.email = user.getEmail();
    }

    @PrePersist
    protected void onCreate() {
        this.joinedAt = LocalDateTime.now();
    }

    public boolean isVerified() {
        return this.user != null;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }
}
