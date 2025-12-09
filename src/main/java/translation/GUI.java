package translation;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;


// TODO Task D: Update the GUI for the program to align with UI shown in the README example.
//            Currently, the program only uses the CanadaTranslator and the user has
//            to manually enter the language code they want to use for the translation.
//            See the examples package for some code snippets that may be useful when updating
//            the GUI.
public class GUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Translator translator = new JSONTranslator();
            CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
            LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();

            // Language section
            JPanel languagePanel = new JPanel();
            languagePanel.add(new JLabel("Language:"));

            JComboBox<String> languageDropdown = new JComboBox<>();
            languageDropdown.setEditable(true);

            for (String languageCode : translator.getLanguageCodes()) {
                languageDropdown.addItem(languageCodeConverter.fromLanguageCode(languageCode));
            }

            languagePanel.add(languageDropdown);

            // Translation Section
            JPanel translationPanel = new JPanel();

            JLabel resultLabelText = new JLabel("Translation:");
            translationPanel.add(resultLabelText);
            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            translationPanel.add(resultLabel);

            // Country Section
            JPanel countryPanel = new JPanel();
            DefaultListModel<String> countryListModel = new DefaultListModel<>();

            for (String countryCode : translator.getCountryCodes()) {
                countryListModel.addElement(countryCodeConverter.fromCountryCode(countryCode));
            }

            JList<String> countryList = new JList<>(countryListModel);
            countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            countryList.setLayoutOrientation(JList.VERTICAL);
            countryList.setVisibleRowCount(10);
            JScrollPane scrollPane = new JScrollPane(countryList);
            countryPanel.add(scrollPane);

            // lambda function for update
            Runnable updateTranslationLogic = () -> {
                String language = languageDropdown.getSelectedItem().toString();
                String country = countryList.getSelectedValue();

                String languageCode = languageCodeConverter.fromLanguage(language);
                String countryCode = countryCodeConverter.fromCountry(country);

                String result = translator.translate(countryCode, languageCode);

                if (result == null) {
                    result = "no translation found!";
                }
                resultLabel.setText(result);
            };

            languageDropdown.addActionListener(e -> updateTranslationLogic.run());

            // adding listener for when the user clicks the submit button
            countryList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    updateTranslationLogic.run();
                }
            });

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel);
            mainPanel.add(translationPanel);
            mainPanel.add(countryPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
