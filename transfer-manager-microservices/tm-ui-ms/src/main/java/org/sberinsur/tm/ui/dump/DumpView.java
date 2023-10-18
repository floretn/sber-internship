package org.sberinsur.tm.ui.dump;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.sberinsur.tm.beans.rest.AllDumpDTO;
import org.sberinsur.tm.dto.entity.test.DumpEntity;
import org.sberinsur.tm.ui.components.navbar.Navbar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.sberinsur.tm.beans.constants.Markers.BUSINESS_MARKER;
import static org.sberinsur.tm.util.security.SecurityUtils.getLoggedUserName;
import static org.sberinsur.tm.util.web.WebUtils.createEndOfDialog;

/**
 * Относится к web слою.
 * Страница с отображением аналитики в таблице на экране пользователя.
 * @author Софронов И.Е.
 */
@Route("dump")
@PageTitle("Dump")
public class DumpView extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(DumpView.class);

    /**
     * Здесь размещаются элементы пользовательского интерфейса.
     */
    public DumpView(@Autowired RestTemplate restTemplate) {
        log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " просматривает страницу аналитики");
        Grid<DumpEntity> analyticsGrid = new Grid<>();

        analyticsGrid.addColumn(DumpEntity::getId).setHeader("ID");
        analyticsGrid.addColumn(DumpEntity::getArchiveName).setHeader("Archive Name");
        analyticsGrid.addColumn(DumpEntity::getArchiveSize).setHeader("Archive Size");
        analyticsGrid.addColumn(DumpEntity::getFileName).setHeader("File Name");
        analyticsGrid.addColumn(DumpEntity::getFileSize).setHeader("File Size");
        analyticsGrid.addColumn(DumpEntity::getEntityName).setHeader("Entity name");
        analyticsGrid.addColumn(DumpEntity::getRowCount).setHeader("Row Count");
        analyticsGrid.addColumn(DumpEntity::getStarted).setHeader("Started Process");
        analyticsGrid.addColumn(DumpEntity::getFinished).setHeader("Finished Process");
        analyticsGrid.addColumn(DumpEntity::getErrorTime).setHeader("Error Time");
        analyticsGrid.addColumn(DumpEntity::getErrorDescription).setHeader("Error Description");
        analyticsGrid.addColumn(DumpEntity::getFileGuid).setHeader("File Guid");
        analyticsGrid.addColumn(DumpEntity::getDeliveryGuid).setHeader("Delivery Guid");
        analyticsGrid.addComponentColumn(analytics -> new Button("", new Icon(VaadinIcon.EYE), (event) -> {
            log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " открыл диалог для просмотра аналитики с id = " +
                    analytics.getId());
            showDialog(analytics);
        })).setHeader("Action");
        List<DumpEntity> list = restTemplate.getForObject("http://tm-dao-ms/dump", AllDumpDTO.class).getDumps();
        analyticsGrid.setItems(list);

        add(new Navbar());
        add(analyticsGrid);
    }

    /**
     * Метод открытия диалога для просмотра аналитики.
     * @param dump аналитика, которую просматривает пользователь.
     */
    private void showDialog(DumpEntity dump) {
        Dialog dialog = new Dialog();
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(new H3("Viewing Dump"));

        TextField idField = new TextField("ID", dump.getId() + "", "");
        idField.setReadOnly(true);
        verticalLayout.add(idField);

        TextField archiveNameField = new TextField("Archive Name", dump.getArchiveName() + "", "");
        archiveNameField.setReadOnly(true);
        verticalLayout.add(archiveNameField);

        TextField archiveSizeField = new TextField("Archive Size", dump.getArchiveSize() + "", "");
        archiveSizeField.setReadOnly(true);
        verticalLayout.add(archiveSizeField);

        TextField fileNameField = new TextField("File Name", dump.getFileName() + "", "");
        fileNameField.setReadOnly(true);
        verticalLayout.add(fileNameField);

        TextField nSizeField = new TextField("File Size", dump.getFileSize() + "", "");
        nSizeField.setReadOnly(true);
        verticalLayout.add(nSizeField);

        TextField entityNameField = new TextField("Entity name", dump.getEntityName() + "", "");
        entityNameField.setReadOnly(true);
        verticalLayout.add(entityNameField);

        TextField rowCountField = new TextField("Row Count", dump.getRowCount() + "", "");
        rowCountField.setReadOnly(true);
        verticalLayout.add(rowCountField);

        TextField startedProcessField = new TextField("Started Process", dump.getStarted() + "", "");
        startedProcessField.setReadOnly(true);
        verticalLayout.add(startedProcessField);

        TextField finishedProcessField = new TextField("Finished Process", dump.getFinished() + "", "");
        finishedProcessField.setReadOnly(true);
        verticalLayout.add(finishedProcessField);

        TextField errorTimeField = new TextField("Error Time", dump.getErrorTime() + "", "");
        errorTimeField.setReadOnly(true);
        verticalLayout.add(errorTimeField);

        TextField errorDescriptionField = new TextField("Error Description", dump.getErrorDescription() + "", "");
        errorDescriptionField.setReadOnly(true);
        verticalLayout.add(errorDescriptionField);

        TextField fileGuid = new TextField("File Guid",
                dump.getFileGuid() + "", "");
        fileGuid.setReadOnly(true);
        verticalLayout.add(fileGuid);

        TextField deliveryGuid = new TextField("Delivery Guid",
                dump.getDeliveryGuid() + "", "");
        deliveryGuid.setReadOnly(true);
        verticalLayout.add(deliveryGuid);

        createEndOfDialog(dialog, verticalLayout, null, this.getClass());
    }
}
