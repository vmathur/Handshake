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
		print 'hello connection'
		con = pycps.Connection('tcp://cloud-us-0.clusterpoint.com:9007', 'users', 'vmmonkey@gmail.com', 'monkey', '100642')

		responses = []

		for device in devices:
			try:
				response = user_db.search(blueooth_address(device))
				responses.append(response)
			except pycps.APIError as e:
				print 'test'
		
		self.write(responses)
		self.finish()