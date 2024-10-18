package assistcontrol.provider;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RequestFilterTest implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) {
        // request ...

    }

}
