FROM node:12.2.0-alpine as build
WORKDIR /app
# COPY package.json package-lock.json ./
COPY package.json ./
RUN npm install
COPY . ./
ARG PHONEBOOK_API_HOST
ENV PHONEBOOK_API_HOST=${PHONEBOOK_API_HOST}
RUN npm run build
#ngnix
FROM nginx:1.16.0-alpine
COPY --from=build /app/build /usr/share/nginx/html
#RUN rm /etc/ngnix/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d
EXPOSE 80
ENTRYPOINT ["nginx","-g","daemon off;"]