/*

@Entity
@Table(name = "retrospectives")
class RetrospectiveEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @OneToOne
    @JoinColumn(name = "sprint_id", nullable = false)
    val sprint: SprintEntity,

    val completedAt: Instant? = null,

    @ElementCollection
    @CollectionTable(name = "retrospective_talking_points", joinColumns = [JoinColumn(name = "retrospective_id")])
    @Column(name = "talking_point")
    val talkingPoints: List<TalkingPointEmbeddable> = emptyList(),

    val teamMood: String? = null,

    @ElementCollection
    @CollectionTable(name = "retrospective_keep_doing", joinColumns = [JoinColumn(name = "retrospective_id")])
    @Column(name = "keep_doing")
    val keepDoing: List<String> = emptyList(),

    @ElementCollection
    @CollectionTable(name = "retrospective_stop_doing", joinColumns = [JoinColumn(name = "retrospective_id")])
    @Column(name = "stop_doing")
    val stopDoing: List<String> = emptyList(),

    @ElementCollection
    @CollectionTable(name = "retrospective_start_doing", joinColumns = [JoinColumn(name = "retrospective_id")])
    @Column(name = "start_doing")
    val startDoing: List<String> = emptyList(),
)
 */


CREATE TABLE retrospectives (
    id              UUID PRIMARY KEY,
    sprint_id       UUID NOT NULL REFERENCES sprints (id) ON DELETE CASCADE,
    completed_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    team_mood       VARCHAR(255)
);

CREATE TABLE retrospective_talking_points (
    retrospective_id    UUID NOT NULL REFERENCES retrospectives (id) ON DELETE CASCADE,
    prompt              TEXT NOT NULL,
    response            TEXT,
    PRIMARY KEY (retrospective_id, prompt)
);

CREATE TABLE retrospective_keep_doing (
    retrospective_id    UUID NOT NULL REFERENCES retrospectives (id) ON DELETE CASCADE,
    item                TEXT NOT NULL,
    PRIMARY KEY (retrospective_id, item)
);

CREATE TABLE retrospective_stop_doing (
    retrospective_id    UUID NOT NULL REFERENCES retrospectives (id) ON DELETE CASCADE,
    item                TEXT NOT NULL,
    PRIMARY KEY (retrospective_id, item)
);

CREATE TABLE retrospective_start_doing (
    retrospective_id    UUID NOT NULL REFERENCES retrospectives (id) ON DELETE CASCADE,
    item                TEXT NOT NULL,
    PRIMARY KEY (retrospective_id, item)
);

