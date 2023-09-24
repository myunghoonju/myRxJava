package practice.noti;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Component
public class SampleJob implements Job {

    private final SampleJobService service;

    public SampleJob(SampleJobService service) {
        this.service = service;
    }

    @Override
    public void execute(JobExecutionContext context) {
        service.test();
    }
}
