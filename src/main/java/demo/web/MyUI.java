package demo.web;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import demo.web.views.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * Created by victor on 8/6/15.
 */
@Theme("valo")
@SpringUI()
public class MyUI extends UI {
    @Autowired
    private SpringViewProvider viewProvider;

    public SpringViewProvider getViewProvider() {
        return viewProvider;
    }

    public void setViewProvider(SpringViewProvider viewProvider) {
        this.viewProvider = viewProvider;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        MVerticalLayout root = new MVerticalLayout().withFullHeight();
        //root.setSizeFull();

        Panel viewContainer = new Panel();
        //viewContainer.setSizeFull();
        //root.setExpandRatio(viewContainer, 1.0f);

        Navigator navigator = new Navigator(this, viewContainer);
        navigator.addProvider(viewProvider);

        MenuBar menuBar = new MenuBar();
        menuBar.addItem(
                "Main",
                FontAwesome.HOME,
                menuItem -> navigator.navigateTo(DefaultView.VIEW_NAME)
        );
        menuBar.addItem(
                "View Scoped View",
                FontAwesome.BOOKMARK_O,
                menuItem -> navigator.navigateTo(ViewScopedView.VIEW_NAME)
        );
        menuBar.addItem(
                "Banks",
                FontAwesome.BANK,
                selectedItem -> navigator.navigateTo(BanksView.VIEW_NAME)
        );
        menuBar.addItem(
                "Accounts",
                FontAwesome.USERS,
                menuItem -> navigator.navigateTo(AccountsView.VIEW_NAME)
        );
        menuBar.addItem(
                "Uplads",
                FontAwesome.UPLOAD,
                menuItem -> navigator.navigateTo(MyImageView.VIEW_NAME)
        );
        /*final CssLayout navigationBar = new CssLayout();
        navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        navigationBar.addComponent(createNavigationButton("View Scoped View",
                ViewScopedView.VIEW_NAME));
        root.addComponent(navigationBar);
        root.addComponent(new Header("Hola!"));
        */
        root.with(menuBar).expand(viewContainer);
        setContent(root);
    }

}
