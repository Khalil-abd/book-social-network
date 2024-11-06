import {HttpHeaders, HttpInterceptorFn} from '@angular/common/http';
import {inject} from '@angular/core';
import {TokenService} from '../token/token.service';

export const httpTokenInterceptor: HttpInterceptorFn = (request, next) => {
    const tokenService = inject(TokenService);
    const token = tokenService.token;
    if (token) {
        const authRequest = request.clone({
            headers: new HttpHeaders({
                Authorization: 'Bearer ' + token
            })
        });
        return next(authRequest);
    }
    return next(request);
};
