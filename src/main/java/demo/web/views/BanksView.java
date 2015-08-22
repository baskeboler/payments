package demo.web.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import demo.jpa.entities.Bank;
import demo.jpa.repositories.BankRepository;
import demo.web.forms.BankForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.vaadin.viritin.SortableLazyList;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.annotation.PostConstruct;

/**
 * Created by victor on 8/18/15.
 */
@SpringView(name = BanksView.VIEW_NAME)
public class BanksView extends MVerticalLayout implements View {
    public static final String VIEW_NAME = "banks";
    private static final int PAGE_SIZE = 50;
    private final MTable<Bank> banksList = new MTable<>(Bank.class)
            .withProperties("id", "name", "address")
            .withColumnHeaders("Id", "Name", "Address")
            .withFullWidth();
    @Autowired
    private BankRepository bankRepository;
    private final Button newBank = new MButton(FontAwesome.PLUS, event -> this.showCreateBankDialog());
    private final Button editBank = new MButton(FontAwesome.EDIT, event -> {
        this.edit(this.banksList.getValue());
    });

    private final Button deleteBank = new ConfirmButton(FontAwesome.MINUS,
            "Are you sure you want to delete this bank?",
            event -> {
                this.bankRepository.delete(this.banksList.getValue());
                this.banksList.setValue(null);
                this.loadList();
            }
    );

    private void showCreateBankDialog() {
        Bank b = new Bank();
        this.edit(b);
    }

    private void edit(Bank b) {
        BankForm form = new BankForm(b);
        form.openInModalPopup();
        form.setSavedHandler(this::saveBank);
        form.setResetHandler(this::resetBank);
    }

    private void resetBank(Bank bank) {
        this.closeWindow();
    }

    private void closeWindow() {
        this.getUI().getWindows().stream().forEach(w -> this.getUI().removeWindow(w));
    }

    private void saveBank(Bank bank) {
        this.bankRepository.save(bank);
        this.closeWindow();
        this.loadList();
    }

    @SuppressWarnings("unchecked")
    public void loadList() {
        this.banksList.setBeans(new SortableLazyList<>(
                (firstRow, sortAscending, property) -> this.bankRepository.findAllBy(
                        new PageRequest(
                                firstRow / BanksView.PAGE_SIZE,
                                BanksView.PAGE_SIZE,
                                sortAscending ? Direction.ASC : Direction.DESC,
                                property == null ? "id" : property
                        )
                ),
                () -> (int) this.bankRepository.count(),
                BanksView.PAGE_SIZE
        ));
        this.adjustButtonState();
    }

    private void adjustButtonState() {
        boolean hasSelection = this.banksList.getValue() != null;
        this.editBank.setEnabled(hasSelection);
        this.deleteBank.setEnabled(hasSelection);
    }

    @Override
    public void enter(ViewChangeEvent event) {

    }

    public BankRepository getBankRepository() {
        return this.bankRepository;
    }

    public void setBankRepository(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @PostConstruct
    public void init() {
        this.addComponents(
                new MHorizontalLayout(this.newBank, this.editBank, this.deleteBank),
                this.banksList
        );
        this.banksList.addMValueChangeListener(event -> this.adjustButtonState());
        this.banksList.setSelectable(true);
        this.loadList();
    }
}
