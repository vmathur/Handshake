from tornado.ioloop import IOLoop
from tornado.web import RequestHandler, Application, url
import pycps
import json
import time
from tornado.ioloop import IOLoop
from tornado.web import RequestHandler, asynchronous
from tornado import gen

class ConnectionRegisterHandler(RequestHandler):
	def post(self):
		user_id = self.get_argument('user_id')
		devices = self.get_argument('devices')
		con = pycps.Connection('tcp://cloud-us-0.clusterpoint.com:9007', 'users', 'vmmonkey@gmail.com', 'monkey', '100642')
		print devices
		responses = []
		devices = devices.split(',')
		for device in devices:
			print device
			try:
				response = con.search(term('1', 'bluetooth_address'))
				print response
				responses.append(response)
			except pycps.APIError as e:
				print 'there was an error'
		
		self.write(responses)
		self.finish()