package org.eclipse.scout.springboot.demo.scout.ui.task;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.springboot.demo.model.Task;
import org.eclipse.scout.springboot.demo.scout.ui.task.AbstractTaskTablePage.Table.AcceptMenu;
import org.eclipse.scout.springboot.demo.spring.service.TaskService;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MyTaskTablePage extends AbstractTaskTablePage {

  @Inject
  private TaskService taskService;

  public MyTaskTablePage() {
    getTable().getResponsibleColumn().setDisplayable(false);
    getTable().getAcceptedColumn().setDisplayable(false);
    getTable().getMenuByClass(AcceptMenu.class).setVisible(false);
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("MyTasks");
  }

  @Override
  protected Collection<Task> getTasks() {
    return taskService.getOwnTasks(getUser());
  }

  @Override
  protected void execPageActivated() {
    reloadPage();
  }
}