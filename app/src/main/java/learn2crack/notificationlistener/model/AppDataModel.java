package learn2crack.notificationlistener.model;

public class AppDataModel {
    private String _name;
    private String _packageName;
    private Boolean _enabled;

    public AppDataModel(final String name, final String packageName, final Boolean enabled)
    {
        _name = name;
        _packageName = packageName;
        _enabled = enabled;
    }

    public String name()
    {
        return _name;
    }

    public void name(String str)
    {
        _name = str;
    }

    public String packageName()
    {
        return _packageName;
    }

    public void packageName(String str)
    {
        _packageName = str;
    }

    public Boolean enabled()
    {
        return _enabled;
    }

    public void enabled(Boolean state)
    {
        _enabled= state;
    }

}
