package net.herit.platform.platform_spring.common.logger;

public class SourceToTarget {
    private static final String EACH_ARROW = "<->";
    private static final String LEFT_ARROW = "<--";
    private static final String RIGHT_ARROW = "-->";
    private static final String EMPTY_ARROW = "   ";

    private final boolean left;
    private final boolean right;
    private final String arrow;
    private final String service;
    private final String target;

    private SourceToTarget(boolean left, boolean right, String arrow, String service, String target) {
        this.left = left;
        this.right = right;
        this.arrow = arrow;
        this.service = service;
        this.target = target;
    }

    public static SourceToTarget Non(String service) {
        return new SourceToTarget(false, false, EMPTY_ARROW, service, "");
    }

    /***
     * <pre>[#{target} <-> #{service}        ]</pre>
     */
    public static SourceToTarget Left(String service, String target) {
        return new SourceToTarget(true, false, EACH_ARROW, service, target);
    }

    /***
     * <pre>[#{target} --> #{source}        ]</pre>
     */
    public static SourceToTarget LeftIn(String service, String target) {
        return new SourceToTarget(true, false, RIGHT_ARROW, service, target);
    }

    /***
     * <pre>[#{target} <-- #{source}        ]</pre>
     */
    public static SourceToTarget LeftOut(String service, String target) {
        return new SourceToTarget(true, false, LEFT_ARROW, service, target);
    }

    /***
     * <pre>[           #{service} <-> #{target}]</pre>
     */
    public static SourceToTarget Right(String service, String target) {
        return new SourceToTarget(false, true, EACH_ARROW, service, target);
    }

    /***
     * <pre>[           #{service} <-- #{target}]</pre>
     */
    public static SourceToTarget RightIn(String service, String target) {
        return new SourceToTarget(false, true, LEFT_ARROW, service, target);
    }

    /***
     * <pre>[           #{service} --> #{target}]</pre>
     */
    public static SourceToTarget RightOut(String service, String target) {
        return new SourceToTarget(false, true, RIGHT_ARROW, service, target);
    }

    @Override
    public String toString() {
        String left = this.left ? getLeftFormat(this.target) + this.arrow : getLeftFormat("") + EMPTY_ARROW;
        String right = this.right ? this.arrow + getRightFormat(this.target) :  EMPTY_ARROW + getRightFormat("");
        return "[" + left + getCenterFormat(this.service) + right + "]";
    }

    private String getLeftFormat(String value) {
        StringBuilder stringBuilder = new StringBuilder(value);
        for (int i = value.length() - 1; i < 10; i++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    private String getRightFormat(String value) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = value.length() - 1; i < 10; i++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.append(value).toString();
    }

    private String getCenterFormat(String value) {
        int length = value.length();
        if(length > 10){
            return value.substring(0, 10);
        }else{
            StringBuilder stringBuilder = new StringBuilder(value);
            for(int i=0; i<10 - length; i++){
                if(i%2 == 0){
                    stringBuilder.insert(0, " ");
                }else{
                    stringBuilder.append(" ");
                }
            }
            return stringBuilder.toString();
        }
    }
}
