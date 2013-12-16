package name.ixr.nano.common.pipeline;

import name.ixr.nano.common.context.PipelineContext;

/**
 * Created by yuuji on 12/16/13.
 */
public interface PipelineValue {
    void invoke(PipelineContext pipelineContext);
}
