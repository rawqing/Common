package yq.test.constant;

public enum Environment {
    OL("ol"), PRE("pre"),TEST("test"),DEV("dev");

    private final String value;
    Environment(String value) {
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
