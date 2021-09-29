package com.tony.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tony.common.SecurityUtil;

@Controller
@RequestMapping(path = "/rest/process", produces = "application/json;charset=UTF-8")
public class ProcessController {

  @Autowired
  private ProcessEngine processEngine;
  @Autowired
  private RepositoryService repositoryService;
  @Autowired
  private SecurityUtil securityUtil;
  @Autowired
  private ProcessRuntime processRuntime;
  @Autowired
  private TaskRuntime taskRuntime;

  @PostMapping("/save")
  @ResponseBody
  public String save(HttpServletRequest request) throws IOException {
    //    processEngine.getRepositoryService().createDeployment()
    //        .disableBpmnValidation()
    //        .addString("test.bpmn",
    //            ServletRequestUtils.getStringParameter(request, "bpmn", ""))
    //        .addString("test.svg",
    //            ServletRequestUtils.getStringParameter(request, "svg", ""))
    //        .name("测试流程").deploy();
    Deployment deployment = repositoryService.createDeployment()
        .disableBpmnValidation()
        .addString("test.bpmn",
            ServletRequestUtils.getStringParameter(request, "bpmn", ""))
        .addString("test.svg",
            ServletRequestUtils.getStringParameter(request, "svg", ""))
        .name("测试流程").deploy();
    ProcessDefinition processDefinition = repositoryService
        .createProcessDefinitionQuery().deploymentId(deployment.getId())
        .singleResult();
    return processDefinition.getId();
  }

  @GetMapping("/listProcess")
  @ResponseBody
  public List<org.activiti.api.process.model.ProcessDefinition> listProcess(
      Integer index, Integer size) {
    securityUtil.logInAs("system");
    Page<org.activiti.api.process.model.ProcessDefinition> page = processRuntime
        .processDefinitions(Pageable.of(index, size));
    return page.getContent();
  }

  @PostMapping("/startProcess")
  @ResponseBody
  public String startProcess(String key) {
    securityUtil.logInAs("system");
    ProcessInstance processInstance = processRuntime
        .start(ProcessPayloadBuilder.start().withProcessDefinitionKey(key)
            .withName("流程实例1").withBusinessKey("业务1").build());
    return processInstance.getId();
  }

  @GetMapping("/listTask")
  @ResponseBody
  public List<Task> listTask(Integer index, Integer size) {
    securityUtil.logInAs("system");
    Page<Task> page = taskRuntime.tasks(Pageable.of(index, size));
    return page.getContent();
  }

}
