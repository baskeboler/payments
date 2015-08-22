package demo.web.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.annotation.PostConstruct;

/**
 * Created by victor on 8/6/15.
 */
@SpringView(name = ViewScopedView.VIEW_NAME)
public class ViewScopedView extends MVerticalLayout implements View {
    public static final String VIEW_NAME = "view";

    @PostConstruct
    public void init() {
        setMargin(true);
        setSpacing(true);
        addComponent(new Label("This is the view scoped view"));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
