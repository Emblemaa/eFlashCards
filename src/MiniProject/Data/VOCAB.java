package MiniProject.Data;

public class VOCAB {

    private String Word;
    private String Definition;
    private boolean isChecked;

    public String getWord()
    {
        return Word;
    }

    public String getDefinition() {
        return Definition;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setWord(String word) {
        Word = word;
    }

    public void setDefinition(String def) {
        Definition = def;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public VOCAB(String word, String def, boolean checked)
    {
        Word = word;
        Definition = def;
        isChecked = checked;
    }
}
