import cryptocode

str_encoded = cryptocode.encrypt("I am okay","wow")
## And then to decode it:
str_decoded = cryptocode.decrypt("+9ZYomJugZqf*ALYFShXwgd0OwxEQDMM+Lw==*+Kv7/Jcq3ZFS5ts0xGXbAw==*nzVy6BBWj4GFupxUYlqrpA==", "wow")
print(str_decoded)