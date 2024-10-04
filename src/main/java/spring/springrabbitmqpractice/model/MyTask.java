package spring.springrabbitmqpractice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;
import org.springframework.util.StringUtils;

public class MyTask {

  private String taskId;
  private String msg;

  @JsonIgnore
  private String DEFAULT_MSG = "you guys do something";

  private MyTask() {}

  public MyTask(String msg) {
    this.taskId = UUID.randomUUID().toString();
    this.msg = StringUtils.hasLength(msg) ? msg : DEFAULT_MSG;
  }

  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  @Override
  public String toString() {
    return "MyTask{" +
        "taskId='" + taskId + '\'' +
        ", msg='" + msg + '\'' +
        '}';
  }
}
