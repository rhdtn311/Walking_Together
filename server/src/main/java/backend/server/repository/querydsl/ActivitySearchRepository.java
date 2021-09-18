package backend.server.repository.querydsl;

import backend.server.entity.Activity;
import com.querydsl.core.Tuple;

import java.time.LocalDate;
import java.util.List;

public interface ActivitySearchRepository {

//    public List<Tuple> activityInfo(String keyword, LocalDate from, LocalDate to, int activityDivision);

//    public List<Tuple> activityDetail(Long activityId);

//    public List<Tuple> feed(String stdId, String sort);

//    public Tuple feedDetail(Long activityId);

    public boolean findDoingActivity(String stdId);
}
