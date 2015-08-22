package demo.web.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import org.vaadin.viritin.label.RichText;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.annotation.PostConstruct;

/**
 * Created by victor on 8/6/15.
 */
@SpringView(name = DefaultView.VIEW_NAME)
public class DefaultView extends MVerticalLayout implements View {
    public static final String VIEW_NAME = "";

    @PostConstruct
    public void init() {
        addComponents(
                new RichText().withMarkDownResource("/static/main.md")
        );
    }
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
