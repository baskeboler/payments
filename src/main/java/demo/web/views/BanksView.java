package demo.web.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import demo.jpa.entities.Bank;
import demo.jpa.repositories.BankRepository;
import demo.web.forms.BankForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.SortableLazyList;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.fields.MValueChangeEvent;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.annotation.PostConstruct;

/**
 * Created by victor on 8/18/15.
 */
@SpringView(name = BanksView.VIEW_NAME)
public class BanksView extends MVerticalLayout implements View {
    @Autowired
    private BankRepository bankRepository;
    public static final String VIEW_NAME = "banks";

    private static final int PAGE_SIZE = 50;

    private final MTable<Bank> banksList = new MTable<>(Bank.class)
            .withProperties("id", "name", "address")
            .withColumnHeaders("Id", "Name", "Address")
            .withFullWidth();
    private final Button newBank = new MButton(FontAwesome.PLUS, event -> showCreateBankDialog());
    private final Button editBank = new MButton(FontAwesome.EDIT, event -> {
        edit(banksList.getValue());
    });

    private final Button deleteBank = new ConfirmButton(FontAwesome.MINUS,
            "Are you sure you want to delete this bank?",
            event -> {
                bankRepository.delete(banksList.getValue());
                banksList.setValue(null);
                loadList();
            }
    );

    private void showCreateBankDialog() {
        Bank b = new Bank();
        edit(b);
    }

    private void edit(Bank b) {
        BankForm form = new BankForm(b);
        form.openInModalPopup();
        form.setSavedHandler(this::saveBank);
        form.setResetHandler(this::resetBank);
    }

    private void resetBank(Bank bank) {
        closeWindow();
    }

    private void saveBank(Bank bank) {
        bankRepository.save(bank);
        closeWindow();
        loadList();
    }

    private void closeWindow() {
        getUI().getWindows().stream().forEach(w -> getUI().removeWindow(w));
    }

    public void loadList() {
        banksList.setBeans(new SortableLazyList<>(
                (firstRow, sortAscending, property) -> bankRepository.findAllBy(
                        new PageRequest(
                                firstRow / PAGE_SIZE,
                                PAGE_SIZE,
                                sortAscending ? Sort.Direction.ASC : Sort.Direction.DESC,
                                property == null ? "id" : property
                        )
                ),
                () -> (int) bankRepository.count(),
                PAGE_SIZE
        ));
        adjustButtonState();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    public BankRepository getBankRepository() {
        return bankRepository;
    }

    public void setBankRepository(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @PostConstruct
    public void init() {
        addComponents(
                new MHorizontalLayout(newBank, editBank, deleteBank),
                banksList
        );
        banksList.addMValueChangeListener(event -> adjustButtonState());
        banksList.setSelectable(true);
        loadList();
    }

    private void adjustButtonState() {
        boolean hasSelection = banksList.getValue() != null;
        editBank.setEnabled(hasSelection);
        deleteBank.setEnabled(hasSelection);
    }
}
