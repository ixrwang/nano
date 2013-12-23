package name.ixr.nano.common.pipeline;

import name.ixr.nano.common.context.PipelineContext;

/**
 * Created by yuuji on 12/16/13.
 */
public abstract class PipelineValue {
    public static enum Result {
        NEXT, BREAK, RETURN
    }

    public Result invoke(PipelineContext pipelineContext) {
        return Result.NEXT;
    }
}
